package hackathon.sprinter.aws.controller

import hackathon.sprinter.aws.model.UploadResponse
import hackathon.sprinter.aws.service.AwsS3UploadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AwsS3Controller(
    private val awsS3UploadService: AwsS3UploadService,
) {
    @PostMapping("/s3-image")
    fun uploadS3Image(@RequestParam image: MultipartFile): ResponseEntity<UploadResponse> {
        return try {
            val awsS3ObjectDto = awsS3UploadService.uploadImageS3(image)
            ResponseEntity.status(HttpStatus.OK).body(
                UploadResponse(
                    response = HttpStatus.OK.name,
                    id = awsS3ObjectDto.id,
                    url = awsS3ObjectDto.url,
                    name = image.originalFilename.toString()
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }
}