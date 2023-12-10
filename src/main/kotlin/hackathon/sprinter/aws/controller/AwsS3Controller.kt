package hackathon.sprinter.aws.controller

import hackathon.sprinter.aws.model.UploadResponse
import hackathon.sprinter.aws.service.AwsS3Service
import hackathon.sprinter.jwt.controller.ResponseData
import hackathon.sprinter.member.service.MemberAuthenticateService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Api("AWS")
@RequestMapping("/api/aws/v1")
class AwsS3Controller(
    @Qualifier("s3Service") private val awsS3Service: AwsS3Service,
    private val authenticateService: MemberAuthenticateService,
) {
    @PostMapping("/s3")
    fun uploadFileS3(@RequestParam file: MultipartFile): ResponseEntity<UploadResponse> {
        val memberId = authenticateService.findMemberPkByAuthentication()
        return try {
            val awsS3ObjectDto = awsS3Service.uploadFileS3(file, memberId)
            ResponseEntity.status(HttpStatus.OK).body(
                UploadResponse(
                    response = HttpStatus.OK.name,
                    id = awsS3ObjectDto.id,
                    url = awsS3ObjectDto.url,
                    name = file.originalFilename.toString(),
                    memberId = memberId
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }

    @DeleteMapping("/s3")
    fun deleteFileS3(@RequestParam url: String): ResponseEntity<ResponseData<String>> {
        return try {
            val memberId = authenticateService.findMemberPkByAuthentication()
            val result = awsS3Service.deleteFileS3(url, memberId.toString())
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseData(result, "삭제 성공", url))
        } catch (e: Exception) {
            throw e
        }
    }
}