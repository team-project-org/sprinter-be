package hackathon.sprinter.member.service

import hackathon.sprinter.configure.CAuthenticationException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.jwt.model.PrincipalUserDetails
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class MemberAuthenticateService {
    fun findMemberPkByAuthentication(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is AnonymousAuthenticationToken || authentication.principal == null) {
            throw CAuthenticationException(ErrorCode.AUTHENTICATE_FAIL, "인증에 실패하였습니다.")
        }
        val currentUser  = authentication.principal as PrincipalUserDetails
        return currentUser.getId()
    }
}