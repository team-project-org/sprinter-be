package hackathon.sprinter.aws.controller

import hackathon.sprinter.aws.model.UploadResponse
import hackathon.sprinter.aws.service.AwsS3UploadService
import hackathon.sprinter.member.service.MemberAuthenticateService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Api("AWS")
@RequestMapping("/api/aws")
class AwsS3Controller(
    private val awsS3UploadService: AwsS3UploadService,
    private val authenticateService: MemberAuthenticateService,
) {
    @PostMapping("/v1/s3-image")
    fun uploadS3Image(@RequestParam image: MultipartFile): ResponseEntity<UploadResponse> {
        val memberId = authenticateService.findMemberPkByAuthentication()
        return try {
            val awsS3ObjectDto = awsS3UploadService.uploadImageS3(image)
            ResponseEntity.status(HttpStatus.OK).body(
                UploadResponse(
                    response = HttpStatus.OK.name,
                    id = awsS3ObjectDto.id,
                    url = awsS3ObjectDto.url,
                    name = image.originalFilename.toString(),
                    memberId = memberId
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }

    @PostMapping("/v1/s3-file")
    fun uploadS3File(@RequestParam file: MultipartFile): ResponseEntity<UploadResponse> {
        val memberId = authenticateService.findMemberPkByAuthentication()
        return try {
            val awsS3ObjectDto = awsS3UploadService.uploadFileS3(file)
            ResponseEntity.status(HttpStatus.OK).body(
                UploadResponse(
                    response = HttpStatus.OK.name,
                    id = awsS3ObjectDto.id,
                    url = awsS3ObjectDto.url,
                    name = file.originalFilename.toString(),
                    memberId = memberId,
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }
}