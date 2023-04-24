package hackathon.sprinter.member.controller

import com.netflix.dgs.codegen.generated.types.CreateMemberInput
import com.netflix.dgs.codegen.generated.types.UpdateProfileNameInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.member.service.MemberService
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class MemberMutationResolver(
    private val memberService: MemberService,
) {
    @DgsMutation
    @PreAuthorize("isAnonymous()")
    fun createMember(
        @InputArgument input: CreateMemberInput
    ): Long {
        return memberService.create(input)
    }

    @DgsMutation
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateProfileName(
        @InputArgument input: UpdateProfileNameInput
    ): Long {
        return memberService.updateProfileName(input)
    }
}