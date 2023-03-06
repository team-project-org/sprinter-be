package hackathon.peerfund.jwt.service

import hackathon.peerfund.jwt.config.JwtConfig
import hackathon.peerfund.member.model.Member
import hackathon.peerfund.member.service.MemberService
import hackathon.peerfund.util.currentKSTDate
import hackathon.peerfund.util.plusKSTDate
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class JwtProviderService(
    private val memberService: MemberService
) : JwtProviderServiceInterface {

    @Value("\${jwt.secret-key}")
    private val secretValue: String? = null
    private fun getSecretKey(): SecretKey = Keys.hmacShaKeyFor(secretValue!!.encodeToByteArray())
    private fun getParser(): JwtParser = Jwts.parserBuilder().setSigningKey(getSecretKey()).build()
    private fun parseToken(token: String) = getParser().parseClaimsJws(token).body

    override fun createAccessToken(username: String): String {
        return Jwts
            .builder()
            .setSubject(JwtConfig.ACCESS)
            .setClaims(mapOf(JwtConfig.USERNAME to username))
            .setIssuedAt(currentKSTDate())
            .setExpiration(plusKSTDate(min = JwtConfig.ACCESS_TOKEN_EXPIRATION))
            .signWith(getSecretKey())
            .compact()
    }

    override fun createRefreshToken(): String {
        return Jwts
            .builder()
            .setSubject(JwtConfig.REFRESH)
            .setIssuedAt(currentKSTDate())
            .setExpiration(plusKSTDate(day = JwtConfig.REFRESH_TOKEN_EXPIRATION))
            .signWith(getSecretKey())
            .compact()
    }

    override fun extractRefreshToken(request: HttpServletRequest): String {
        return request
            .getHeader(JwtConfig.REFRESH_TOKEN_HEADER)
            .replace(JwtConfig.TOKEN_PREFIX, "")
    }

    override fun extractAccessToken(request: HttpServletRequest): String {
        return request
            .getHeader(JwtConfig.REFRESH_TOKEN_HEADER)
            .replace(JwtConfig.TOKEN_PREFIX, "")
    }

    override fun checkExpiredToken(token: String): Boolean {
        return try {
            parseToken(token)
            false
        } catch (e: ExpiredJwtException) {
            true
        } catch (e: Exception) {
            throw e
        }
    }

    override fun checkExpireInSevenDayToken(token: String): Boolean {
        return parseToken(token).expiration.before(plusKSTDate(day = 7))
    }

    override fun checkValidHeader(request: HttpServletRequest): Boolean {
        request
            .also { it.getHeader(JwtConfig.REFRESH_TOKEN_HEADER)?.startsWith(JwtConfig.TOKEN_PREFIX) ?: return false }
            .also { it.getHeader(JwtConfig.ACCESS_TOKEN_HEADER)?.startsWith(JwtConfig.TOKEN_PREFIX) ?: return false }
        return true
    }

    override fun checkValidToken(token: String): Boolean {
        return try {
            parseToken(token)
            true
        } catch (e: ExpiredJwtException) {
            true
        } catch (e: JwtException) {
            false
        }
    }

    override fun findMemberByToken(token: String): Member {
        return memberService.findMemberByToken(token)
    }

    override fun findMemberByUsername(username: String): Member {
        return memberService.findMemberByUsername(username)
    }

    @Transactional
    override fun saveRefreshToken(username: String, token: String) {
        memberService
            .findMemberByUsername(username)
            .updateToken(token)
    }

    @Transactional
    override fun reissueRefreshToken(username: String): String {
        val reissuedRefreshToken = createRefreshToken()
        memberService
            .findMemberByUsername(username)
            .updateToken(reissuedRefreshToken)
        return reissuedRefreshToken

    }

    override fun setResponseMessage(result: Boolean, response: HttpServletResponse, message: String) {
        response.contentType = "application/json;charset=UTF-8"
        val content = JSONObject()
            .apply { put("success", result) }
            .apply { put("message", message) }
        response
            .writer
            .print(content)
    }

    fun setHeaderOfAccessToken(response: HttpServletResponse, token: String) {
        response.addHeader(JwtConfig.ACCESS_TOKEN_HEADER, JwtConfig.TOKEN_PREFIX + token)
    }

    fun setHeaderOfRefreshToken(response: HttpServletResponse, token: String) {
        response.addHeader(JwtConfig.REFRESH_TOKEN_HEADER, JwtConfig.TOKEN_PREFIX + token)
    }
}