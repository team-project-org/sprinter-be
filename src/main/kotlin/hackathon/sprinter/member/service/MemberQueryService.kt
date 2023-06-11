package hackathon.sprinter.member.service

import hackathon.sprinter.post.model.PostDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberQueryService(
    private val memberService: MemberService,
) {
    @Transactional(readOnly = true)
    fun getOwnerPostList(memberId: Long): List<PostDto> {
        val member = memberService.findMemberById(memberId)
        return member.ownerPostList.map { PostDto.from(it) }
    }
}