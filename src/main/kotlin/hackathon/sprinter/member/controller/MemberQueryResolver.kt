package hackathon.sprinter.member.controller

import com.netflix.dgs.codegen.generated.types.MemberList
import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.PostList
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.configure.dto.ID
import hackathon.sprinter.member.service.MemberAuthenticateService
import hackathon.sprinter.member.service.MemberQueryService
import org.springframework.security.access.prepost.PreAuthorize


@DgsComponent
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
class MemberQueryResolver(
    private val memberQueryService: MemberQueryService,
    private val memberAuthenticateService: MemberAuthenticateService,
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
    @PreAuthorize("hasRole('ADMIN')")
    fun getMemberAdmin(
        @InputArgument(name = "member_id") memberId: ID
    ): MemberResponse {
        return memberQueryService.findMemberResponseById(memberId.toLong())
    }

    @DgsQuery
    fun getMe(): MemberResponse {
        val userPk = memberAuthenticateService.findMemberPkByAuthentication()
        return memberQueryService.findMemberResponseById(userPk)
    }

    @DgsQuery
    fun getMember(
        @InputArgument(name = "member_id") memberId: ID
    ): MemberResponse {
        return memberQueryService.findMemberResponseById(memberId.toLong())
    }

    @DgsQuery
    fun getAllMemberList(): MemberList {
        val memberResponseList = memberQueryService.findAllMemberResponse()
        return MemberList(
            total_count = memberResponseList.size,
            item_list = memberResponseList
        )
    }

    @DgsQuery
    fun getMyPostList(): PostList {
        val memberPk = memberAuthenticateService.findMemberPkByAuthentication()
        val ownerPostList = memberQueryService.getOwnerPostResponseList(memberPk)
        return PostList(
            total_count = ownerPostList.size,
            item_list = ownerPostList
        )
    }

    @DgsQuery
    fun getMemberPostList(
        @InputArgument(name = "member_id") memberId: ID
    ): PostList {
        val ownerPostListResponse = memberQueryService.getOwnerPostResponseList(memberId.toLong())
        return PostList(
            total_count = ownerPostListResponse.size,
            item_list = ownerPostListResponse
        )
    }
}
