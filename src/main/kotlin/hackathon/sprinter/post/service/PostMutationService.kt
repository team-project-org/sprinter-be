package hackathon.sprinter.post.service

import hackathon.sprinter.member.service.MemberQueryService
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.Post
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Service
class PostMutationService(
    private val memberQueryService: MemberQueryService,
) {
    @Transactional
    fun createPost(
        title: String,
        startDate: OffsetDateTime?,
        endDate: OffsetDateTime?,
        ownerUsername: String
    ): Post {
        val owner = memberQueryService.findMemberByUsername(ownerUsername)
        val post = Post(
            title = title,
            startDate = startDate,
            endDate = endDate,
            memberPostList = mutableListOf(),
        )
        val memberPost = MemberPost(owner, post)

        return owner.createPost(memberPost, post)
    }
}