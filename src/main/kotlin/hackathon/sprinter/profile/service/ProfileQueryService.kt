package hackathon.sprinter.profile.service

import com.netflix.dgs.codegen.generated.types.ProfileResponse
import hackathon.sprinter.member.service.MemberAuthenticateService
import hackathon.sprinter.profile.repository.ProfileJpaRepository
import hackathon.sprinter.util.toGqlSchema
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileQueryService(
    private val profileJpaRepository: ProfileJpaRepository,
    private val authenticateService: MemberAuthenticateService,
) {
    @Transactional(readOnly = true)
    fun getMyProfile(): ProfileResponse {
        val memberId = authenticateService.findMemberPkByAuthentication()
        return profileJpaRepository.findByMemberId(memberId).toGqlSchema()
    }
}