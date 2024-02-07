package hackathon.sprinter.post.service

import hackathon.sprinter.post.model.MockPost
import hackathon.sprinter.post.model.Post
import hackathon.sprinter.post.repository.MockPostRepository
import hackathon.sprinter.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostQueryService(
    private val postRepository: PostRepository,
    private val mockPostRepository: MockPostRepository
) {
    @Transactional(readOnly = true)
    fun getAllPostList(): List<Post> {
        return postRepository.findAllByDateDeletedIsNull()
    }

    @Transactional(readOnly = true)
    fun getAllMockPostList(): List<MockPost> {
        return mockPostRepository.findAllByDateDeletedIsNull()
    }
}