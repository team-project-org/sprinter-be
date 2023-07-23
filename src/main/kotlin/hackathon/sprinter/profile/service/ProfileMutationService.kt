package hackathon.sprinter.profile.service

import com.netflix.dgs.codegen.generated.types.CreateProfileInput
import hackathon.sprinter.member.service.MemberQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileMutationService(
    private val profileCreator: ProfileCreator,
    private val memberQueryService: MemberQueryService,
) {
    @Transactional
    fun makeMemberProfile(memberId: Long, input: CreateProfileInput) {
        val profile = profileCreator.create(input)
        val member = memberQueryService.findMemberById(memberId)

        member.updateMyProfile(profile)
    }
}