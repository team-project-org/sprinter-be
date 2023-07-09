package hackathon.sprinter.jwt.handler

import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.jwt.config.JwtConfig
import hackathon.sprinter.jwt.service.JwtProviderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAccessDeniedHandler(
    private val jwtProviderService: JwtProviderService
) : AccessDeniedHandler {
    private val log: Logger = LoggerFactory.getLogger(this::class.simpleName)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        log.error("[권한 오류] ${accessDeniedException.message}");
        val exceptionMessage = request.getAttribute(JwtConfig.EXCEPTION).toString()
        jwtProviderService.setResponseMessage(false, response, ErrorCode.ACCESS_DENIED, exceptionMessage)
    }
}