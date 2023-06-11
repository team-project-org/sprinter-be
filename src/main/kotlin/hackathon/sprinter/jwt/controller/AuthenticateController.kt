package hackathon.sprinter.jwt.controller

import com.netflix.dgs.codegen.generated.types.LoginInput
import com.netflix.dgs.codegen.generated.types.SignupInput
import hackathon.sprinter.member.creator.MemberCreator
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api("Auth")
class AuthenticateController(
    private val memberCreator: MemberCreator,
) {
    @PostMapping("/api/v1/login")
    fun memberLogin(@RequestBody input: LoginInput): ResponseEntity<ResponseData<String>> {
        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseData(true, "로그인 성공", input.username))
        } catch (e: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseData(false, "로그인 실패", "${e.message}"))
        }
    }

    @PostMapping("/api/v1/sign-up")
    fun memberSignUp(@RequestBody input: SignupInput): ResponseEntity<ResponseData<String>> {
        val memberId = memberCreator.create(input)
        return try {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseData(true, "회원가입 성공", "$memberId"))
        } catch (e: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseData(false, "회원가입 실패", "${e.message}"))
        }
    }
}

data class ResponseData<T>(
    val success: Boolean,
    val message: String,
    val data: T
)