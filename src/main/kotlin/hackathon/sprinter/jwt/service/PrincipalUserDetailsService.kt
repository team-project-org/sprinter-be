package hackathon.sprinter.jwt.service

import hackathon.sprinter.configure.MemberNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.jwt.model.PrincipalUserDetails
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.repository.MemberRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class PrincipalUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {
    private val log: Logger = LoggerFactory.getLogger(this::class.simpleName)

    @Throws(MemberNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        log.info("<UserPrincipal 조회 후 반환>")

        val member: Member = memberRepository.findMemberByUsername(username)
            ?: throw MemberNotFoundException(ErrorCode.MEMBER_NOT_EXIST, "회원이 존재하지 않습니다. $username")

        return PrincipalUserDetails(member)
    }
}