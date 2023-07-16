package hackathon.sprinter.profile.resolver

import com.netflix.dgs.codegen.generated.types.CreateProfileInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import hackathon.sprinter.member.service.MemberAuthenticateService
import hackathon.sprinter.profile.service.ProfileMutationService

@DgsComponent
class ProfileMutationResolver(
    private val authenticateService: MemberAuthenticateService,
    private val profileMutationService: ProfileMutationService,
) {
    @DgsMutation
    fun createMemberProfile(input: CreateProfileInput): Long {
        val memberId = authenticateService.findMemberPkByAuthentication()
        profileMutationService.makeMemberProfile(memberId, input)
        return memberId
    }
}