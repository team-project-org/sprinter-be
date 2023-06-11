package hackathon.sprinter.member.controller

import com.netflix.dgs.codegen.generated.types.MemberList
import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.PostList
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.configure.dto.ID
import hackathon.sprinter.member.service.MemberQueryService
import hackathon.sprinter.member.service.MemberService
import hackathon.sprinter.util.toGqlSchema
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
class MemberQueryResolver(
    private val memberService: MemberService,
    private val memberQueryService: MemberQueryService,
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
    fun getMember(
        @InputArgument(name = "member_id") memberId: ID
    ): MemberResponse {
        return memberService.findMemberDtoById(memberId.toLong()).toGqlSchema()
    }

    @DgsQuery
    fun getAllMemberList(): MemberList {
        val memberList = memberService.findAllMember()
        return MemberList(
            total_count = memberList.size,
            item_list = memberList.map { it.toGqlSchema() }
        )
    }

    @DgsQuery
    fun getOwnerPostList(
        @InputArgument(name = "member_id") memberId: ID
    ): PostList {
        val ownerPostList = memberQueryService.getOwnerPostList(memberId.toLong())
        return PostList(
            total_count = ownerPostList.size,
            item_list = ownerPostList.map { it.toGqlSchema() }
        )
    }

}
