package hackathon.sprinter.member.controller

import com.netflix.dgs.codegen.generated.types.CreateMemberInput
import com.netflix.dgs.codegen.generated.types.Member
import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.UpdateProfileNameInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.configure.dto.ID
import hackathon.sprinter.member.service.MemberService
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class MemberQueryResolver(
    private val memberService: MemberService,
) {
    @DgsQuery
    @PreAuthorize("hasRole('USER')")
    fun getQueryUserAuth(): Boolean {
        return true
    }

    @DgsQuery
    @PreAuthorize("hasRole('ADMIN')")
    fun getQueryAdminAuth(): Boolean {
        return true
    }

    @DgsQuery
    @PreAuthorize("hasRole('ANONYMOUS')")
    fun getQueryAnonymousAuth(): Boolean {
        return true
    }

    @DgsQuery
    @PreAuthorize("isAnonymous()")
    fun getQueryAnonymous2Auth(): Boolean {
        return true
    }

    @DgsQuery
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
    fun getMember(
        @InputArgument(name = "member_id") memberId: ID
    ): Member {
        return memberService.findMemberById(memberId.toLong())
    }

    @DgsQuery
    fun getAllMemberList(): List<MemberResponse> {
        return memberService.findAllMember()
    }
}
