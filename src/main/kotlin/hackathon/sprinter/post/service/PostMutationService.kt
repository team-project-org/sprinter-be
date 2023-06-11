package hackathon.sprinter.post.service

import hackathon.sprinter.member.service.MemberService
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.Post
import hackathon.sprinter.util.convertToKSTDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostMutationService(
    private val memberService: MemberService,
) {
    @Transactional
    fun createPost(
        title: String,
        startDate: Long,
        endDate: Long,
        ownerUsername: String
    ): Post {
        val owner = memberService.findMemberByUsername(ownerUsername)
        val post = Post(
            title = title,
            startDate = convertToKSTDate(startDate),
            endDate = convertToKSTDate(endDate),
            memberPostList = mutableListOf(),
        )
        val memberPost = MemberPost(owner, post)

        return owner.createPost(memberPost, post)
    }
}