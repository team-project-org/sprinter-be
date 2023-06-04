package hackathon.sprinter.post.service

import hackathon.sprinter.member.service.MemberService
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.Post
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class PostMutationService(
    private val memberService: MemberService,
) {
    @Transactional
    fun createPost(
        title: String,
        startDate: OffsetDateTime,
        endDate: OffsetDateTime,
        ownerUsername: String
    ): Long {
        val owner = memberService.findMemberByUsername(ownerUsername)
        val post = Post(
            title = title,
            startDate = startDate,
            endDate = endDate,
            memberPostList = mutableListOf(),
            ownerMember = owner
        )
        val memberPost = MemberPost(owner, post)
        owner.createPost(memberPost, post)

        return post.id
    }
}