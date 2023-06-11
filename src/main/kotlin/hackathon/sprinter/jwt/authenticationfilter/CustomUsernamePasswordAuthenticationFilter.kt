package hackathon.sprinter.jwt.authenticationfilter

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.dgs.codegen.generated.types.LoginInput
import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.JwtException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.jwt.model.PrincipalUserDetails
import hackathon.sprinter.jwt.service.JwtProviderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomUsernamePasswordAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtProviderService: JwtProviderService
) : UsernamePasswordAuthenticationFilter() {

    private val log: Logger = LoggerFactory.getLogger(this::class.simpleName)

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        log.info("<로그인 시 Authentication(인증) 시도>")
        try {
            val om = ObjectMapper()
            val loginInput = om.readValue(request.inputStream, LoginInput::class.java)
            val authentication = UsernamePasswordAuthenticationToken(loginInput.username, loginInput.password)
            return authenticationManager.authenticate(authentication)
        } catch (e: IOException) {
            log.error("[인증 시도 실패]: ${e.message}")
            throw DataNotFoundException(ErrorCode.ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        try {
            val principal: PrincipalUserDetails = authResult!!.principal as PrincipalUserDetails

            val accessToken: String = jwtProviderService.createAccessToken(principal.username)
            val refreshToken: String = jwtProviderService.createRefreshToken()

            jwtProviderService.saveRefreshToken(principal.username, refreshToken)

            jwtProviderService.setHeaderOfAccessToken(response, accessToken)
            jwtProviderService.setHeaderOfRefreshToken(response, refreshToken)

            jwtProviderService.setResponseMessage(true, response, "로그인 성공", principal.getId())
            log.info("[인증 성공] JWT 발급")
        } catch (e: Exception) {
            log.error("[토큰 발급 실패]: ${e.message}")
            throw JwtException(ErrorCode.JWT_CREATE_FAIL, "jwt 토큰 발급에 실패하였습니다. 에러: ${e.message}")
        }
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        failed: AuthenticationException?
    ) {
        log.info("[인증 실패]")
        val failMessage = when (failed!!.message) {
            ErrorCode.ITEM_NOT_EXIST.name -> ErrorCode.ITEM_NOT_EXIST.name
            ErrorCode.WRONG_PASSWORD.name -> ErrorCode.WRONG_PASSWORD.name
            else -> failed.message
        }
        jwtProviderService.setResponseMessage(false, response, "로그인 실패: $failMessage")
    }
}