package hackathon.sprinter.member.service

import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.PostResponse
import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode.ITEM_NOT_EXIST
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.repository.MemberRepository
import hackathon.sprinter.mockmember.model.MockMember
import hackathon.sprinter.profile.repository.mysql.LinkRepository
import hackathon.sprinter.util.toGqlSchema
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberQueryService(
    private val memberRepository: MemberRepository,
    private val linkRepository: LinkRepository,
) {
    @Transactional(readOnly = true)
    fun findMemberById(memberId: Long): Member {
        return memberRepository.findMemberById(memberId)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun findMemberByToken(token: String): Member {
        return memberRepository.findMemberByToken(token)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun findMemberByUsername(username: String): Member {
        return memberRepository.findMemberByUsername(username)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun findMemberResponseById(memberId: Long): MemberResponse {
        return findMemberById(memberId).toGqlSchema()
    }

    @Transactional(readOnly = true)
    fun findAllMemberResponse(): List<MemberResponse> {
        return memberRepository.findAllByDateDeletedIsNull().map { it.toGqlSchema() }
    }

    @Transactional(readOnly = true)
    fun getOwnerPostResponseList(memberId: Long): List<PostResponse> {
        return findMemberById(memberId).ownerPostList.map { it.toGqlSchema() }
    }

    @Transactional(readOnly = true)
    fun getMockMemberList(): List<MockMember> {
        val mockMemberList = memberRepository.findMockMember()
        val profileIdList = mockMemberList.map { it.profile.id }

        val profileLinkListMap = linkRepository
            .findPortfolioLinkListInProfileIds(profileIdList)
            .groupBy { it.profile!!.id }

        return mockMemberList.map {
            val linkList = profileLinkListMap[it.profile.id] ?: emptyList()
            MockMember.from(
                member = it,
                linkList = linkList
            )
        }
    }
}


@Service
class MemberAssembler(
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun assemble(memberId: Long): MemberResponse {
        val member = memberRepository.findMemberById(memberId)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")

        return MemberResponse(
            id = member.id.toString(),
            username = member.username,
            profile_name = member.profileName,
            role_type_list = member.roles.map { it.roleType }
        )
    }
}
