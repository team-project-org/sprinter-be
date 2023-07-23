package hackathon.sprinter.aws.service

import com.amazonaws.services.s3.model.*
import com.amazonaws.services.securitylake.model.S3Exception
import com.amazonaws.util.IOUtils
import hackathon.sprinter.aws.config.AwsS3Client
import hackathon.sprinter.aws.model.AwsS3Object
import hackathon.sprinter.aws.model.AwsS3ObjectDto
import hackathon.sprinter.aws.repository.AwsS3Repository
import hackathon.sprinter.configure.ParameterInvalidException
import hackathon.sprinter.configure.dto.ErrorCode
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
class AwsS3Service(
    private val awsS3Repository: AwsS3Repository,
    private val awsS3Client: AwsS3Client,
) {
    companion object {
        private const val USER_METADATA_KEY = "x-amz-meta-member_id"
    }

    @Value("\${aws.s3-bucket.sprinter.folder}")
    private lateinit var folderName: String

    @Value("\${aws.s3-bucket.sprinter.bucket}")
    private lateinit var bucketName: String

    @Value("\${aws.s3-bucket.region}")
    private lateinit var regionName: String

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    @Transactional
    fun uploadFileS3(file: MultipartFile, memberId: Long): AwsS3ObjectDto {
        val fileName = file.originalFilename ?: "none"
        val postfix = when (val extension = getExtension(fileName)) {
            "jpg" -> "image/jpeg"
            "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "pdf" -> "pdf"
            "" -> ""
            else -> throw IllegalArgumentException("`${extension}`은 올바른 확장자가 아닙니다.")
        }
        val bytes = IOUtils.toByteArray(file.inputStream)
        val metadata = makeMetadata(postfix, bytes, memberId)
        return putObject(bucketName, fileName, ByteArrayInputStream(bytes), metadata)
    }

    private fun getExtension(fileName: String): String = fileName.split(".").last()

    private fun makeMetadata(
        postfix: String,
        bytes: ByteArray,
        memberId: Long,
    ): ObjectMetadata {
        val metadata = ObjectMetadata()
        metadata.contentDisposition = "inline"
        metadata.contentType = "application/$postfix"
        metadata.contentLength = bytes.size.toLong()
        metadata.userMetadata = mutableMapOf(USER_METADATA_KEY to memberId.toString())
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
        logger.info("UPLOAD FILE TO URL: $url")

        try {
            upload(bucketName, url, inputStream, metadata)
            val s3Url = "${getAwsS3UrlPrefix()}$url"
            val awsS3Object = awsS3Repository.save(AwsS3Object(s3Url))
            return AwsS3ObjectDto(
                awsS3Object.id,
                awsS3Object.url
            )
        } catch (e: Exception) {
            throw S3Exception("AWS S3 업로드에 실패하였습니다.\n${e.message}")
        }
    }

    private fun upload(
        bucketName: String,
        url: String,
        inputStream: InputStream,
        metadata: ObjectMetadata
    ) {
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

    @Transactional
    fun deleteFileS3(url: String, memberId: String): Boolean {
        val key = convertUrlToS3Key(url)
        validateUserMetadata(key, memberId)
        return deleteObject(key)
    }

    private fun convertUrlToS3Key(url: String): String {
        val prefix = getAwsS3UrlPrefix()
        return if (prefix in url) url.replaceFirst(prefix, "")
        else throw ParameterInvalidException(ErrorCode.INVALID_PARAMETER, "`$url` 전달된 URL 값이 올바르지 않습니다.")
    }

    private fun getAwsS3UrlPrefix(): String = "https://$bucketName.s3.$regionName.amazonaws.com/"

    private fun validateUserMetadata(key: String, memberId: String) {
        val value = getFileUserMetadataValue(key)

        if (value != memberId)
            throw ParameterInvalidException(ErrorCode.ACCESS_DENIED, "`$memberId` 본인의 업로드 파일만 지울 수 있습니다.")
    }

    private fun deleteObject(key: String): Boolean {
        return try {
            deleteFileFromS3(key)
            awsS3Repository.deleteByUrl(getAwsS3UrlPrefix() + key)
            true
        } catch (e: Exception) {
            throw S3Exception("AWS S3 파일 제거에 실패했습니다. ${e.message}")
        }
    }

    private fun getFileUserMetadataValue(key: String) = awsS3Client
        .getAwsS3Client()
        .getObject(
            GetObjectRequest(
                bucketName,
                key
            )
        )
        .objectMetadata
        .userMetadata[USER_METADATA_KEY]

    private fun deleteFileFromS3(key: String) {
        awsS3Client
            .getAwsS3Client()
            .deleteObject(
                DeleteObjectRequest(
                    bucketName,
                    key
                )
            )
    }
}