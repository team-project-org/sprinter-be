package hackathon.sprinter.member.controller

import com.netflix.dgs.codegen.generated.types.SignupInput
import com.netflix.dgs.codegen.generated.types.UpdateProfileNameInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.member.service.MemberService
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
class MemberMutationResolver(
    private val memberService: MemberService,
) {
    @DgsMutation
    fun createMember(
        @InputArgument input: SignupInput
    ): Long {
        return memberService.create(input)
    }

    @DgsMutation
    fun updateProfileName(
        @InputArgument input: UpdateProfileNameInput
    ): Long {
        return memberService.updateProfileName(input)
    }
}