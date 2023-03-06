package hackathon.peerfund.jwt.authenticationfilter

import hackathon.peerfund.jwt.config.JwtConfig
import hackathon.peerfund.jwt.model.PrincipalUserDetails
import hackathon.peerfund.jwt.service.JwtProviderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomJwtAuthenticationFilter(
    private val jwtProviderService: JwtProviderService,
) : GenericFilterBean() {
    private val log: Logger = LoggerFactory.getLogger(this::class.simpleName)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        log.info("<Authentication(인증) 필터>")
        try {
            request as HttpServletRequest
            response as HttpServletResponse
            request
                .apply {
                    if (jwtProviderService.checkValidHeader(this).not()) {
                        chain.doFilter(request, response)
                        return
                    }
                }
                .let {
                    val accessToken = jwtProviderService.extractAccessToken(request)
                    val refreshToken = jwtProviderService.extractRefreshToken(request)
                    TokenPair(accessToken, refreshToken)
                }
                .apply {
                    val authentication = getAuthentication(response)
                    SecurityContextHolder.getContext().authentication = authentication
                }
        } catch (jwtException: AuthenticationException) {
            log.info("[인가 실패]")
            request.setAttribute(JwtConfig.EXCEPTION, jwtException.message)
        } catch (e: Exception) {
            log.info("[미정의 에러]")
            e.printStackTrace()
            request.setAttribute(JwtConfig.EXCEPTION, e.message)
        }
        chain.doFilter(request, response)
    }

    private fun TokenPair.getAuthentication(response: HttpServletResponse): UsernamePasswordAuthenticationToken {
        check(jwtProviderService.checkValidToken(this.access))
        check(jwtProviderService.checkValidToken(this.refresh))
        check(jwtProviderService.checkExpiredToken(this.refresh).not())

        val member = jwtProviderService.findMemberByToken(this.refresh)

        val expired = jwtProviderService.checkExpiredToken(this.access)
        if (expired) reissueAccessToken(member.username, response)

        val expireIn7Day = jwtProviderService.checkExpireInSevenDayToken(this.refresh)
        if (expireIn7Day) reissueRefreshToken(member.username, response)

        val principal = PrincipalUserDetails(member)
        return UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
    }

    private fun reissueAccessToken(
        username: String,
        response: HttpServletResponse
    ) {
        log.info("[ACCESS TOKEN] 액세스 토큰 재발급")
        val reissuedAccessToken = jwtProviderService.createAccessToken(username)
        jwtProviderService.setHeaderOfAccessToken(response, reissuedAccessToken)
    }

    private fun reissueRefreshToken(
        username: String,
        response: HttpServletResponse
    ) {
        log.info("[REFRESH TOKEN] 리프레쉬 토큰 재발급")
        val reissuedRefreshToken = jwtProviderService.reissueRefreshToken(username)
        jwtProviderService.setHeaderOfRefreshToken(response, reissuedRefreshToken)
    }
}

data class TokenPair(
    val access: String,
    val refresh: String,
)