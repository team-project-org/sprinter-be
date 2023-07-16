package hackathon.sprinter.aws.service

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.StorageClass
import com.amazonaws.services.securitylake.model.S3Exception
import com.amazonaws.util.IOUtils
import hackathon.sprinter.aws.config.AwsS3Client
import hackathon.sprinter.aws.model.AwsS3Object
import hackathon.sprinter.aws.model.AwsS3ObjectDto
import hackathon.sprinter.aws.repository.AwsS3Repository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.time.OffsetDateTime
import java.util.*

@Service
class AwsS3UploadService(
    private val awsS3Repository: AwsS3Repository,
    private val awsS3Client: AwsS3Client,
) {
    @Value("\${aws.s3-bucket.sprinter.folder}")
    private lateinit var folderName: String

    @Value("\${aws.s3-bucket.sprinter.bucket}")
    private lateinit var bucketName: String

    @Value("\${aws.s3-bucket.region}")
    private lateinit var regionName: String

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @Transactional
    fun uploadImageS3(file: MultipartFile): AwsS3ObjectDto {
        val fileName = file.originalFilename ?: "null"
        val postfix = when (val extension = fileName.substringAfter(".")) {
            "jpg" -> "image/jpeg"
            "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "" -> "image/jpeg"
            else -> throw IllegalArgumentException("${extension}은 이미지 확장자가 아닙니다.")
        }
        val bytes = IOUtils.toByteArray(file.inputStream)
        val metadata = getMetadata(postfix, bytes)
        return putObject(bucketName, fileName, ByteArrayInputStream(bytes), metadata)
    }

    @Transactional
    fun uploadFileS3(file: MultipartFile): AwsS3ObjectDto {
        val fileName = file.originalFilename ?: "null"
        val postfix = when (val extension = fileName.substringAfter(".")) {
            "pdf" -> "pdf"
            else -> extension
        }
        val bytes = IOUtils.toByteArray(file.inputStream)
        val metadata = getMetadata(postfix, bytes)
        return putObject(bucketName, fileName, ByteArrayInputStream(bytes), metadata)
    }

    private fun getMetadata(
        postfix: String,
        bytes: ByteArray
    ): ObjectMetadata {
        val metadata = ObjectMetadata()
        metadata.contentDisposition = "inline"
        metadata.contentType = "application/$postfix"
        metadata.contentLength = bytes.size.toLong()
        return metadata
    }

    fun putObject(
        bucketName: String,
        fileName: String,
        inputStream: InputStream,
        metadata: ObjectMetadata
    ): AwsS3ObjectDto {
        val uuid = generateUniqueKey(fileName)
        val url = getUrl(uuid)
        logger.info("UPLOAD IMAGE TO URL: $url")

        try {
            awsS3Client
                .getAwsS3Client()
                .putObject(
                    PutObjectRequest(
                        bucketName,
                        url,
                        inputStream,
                        metadata
                    ).withStorageClass(StorageClass.IntelligentTiering)
                )
            val s3Url = "https://$bucketName.s3.$regionName.amazonaws.com/$url"
            val awsS3Object = awsS3Repository.save(AwsS3Object(s3Url))
            return AwsS3ObjectDto(
                awsS3Object.id,
                awsS3Object.url
            )
        } catch (e: Exception) {
            throw S3Exception("AWS S3 업로드에 실패하였습니다.\n${e.message}")
        }
    }

    private fun generateUniqueKey(originalFilename: String): String {
        return "${UUID.randomUUID()}-$originalFilename"
    }

    private fun getUrl(uuid: String): String {
        val currentTime = OffsetDateTime.now()
        val year = currentTime.year
        val month = currentTime.month
        val day = currentTime.dayOfMonth

        return "$folderName/$year/$month/$day/$uuid"
    }
}