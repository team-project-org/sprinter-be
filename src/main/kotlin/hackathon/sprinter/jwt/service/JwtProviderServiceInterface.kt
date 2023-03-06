package hackathon.sprinter.jwt.service

import hackathon.sprinter.member.model.Member
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface JwtProviderServiceInterface {
    fun createAccessToken(username: String): String // Access Token 생성
    fun createRefreshToken(): String // Refresh Token 생성

    fun extractRefreshToken(request: HttpServletRequest): String // Header 에서 Refresh Token 추출
    fun extractAccessToken(request: HttpServletRequest): String // Header 에서 Access Token 추출

    fun checkValidToken(token: String): Boolean // 토큰 검증
    fun checkValidHeader(request: HttpServletRequest): Boolean // request header 유효성 체크
    fun checkExpiredToken(token: String): Boolean // 토큰 만료 여부 검증
    fun checkExpireInSevenDayToken(token: String): Boolean // 토큰 1주일 이내 만료 여부 검증

    fun findMemberByToken(token: String): Member // refresh token 으로 회원 조회
    fun findMemberByUsername(username: String): Member // username 으로 회원 조회

    fun saveRefreshToken(username: String, token: String) // member to refresh Token 저장
    fun reissueRefreshToken(username: String): String // member Refresh 토큰 업데이트
    fun setResponseMessage(result: Boolean, response: HttpServletResponse, message: String)
}
