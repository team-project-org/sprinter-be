package hackathon.sprinter.post.service

import hackathon.sprinter.post.model.Post
import hackathon.sprinter.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostQueryService(
    private val postRepository: PostRepository,
) {
    @Transactional(readOnly = true)
    fun getAllPostList(): List<Post> {
        return postRepository.findAllByDateDeletedIsNull()
    }
}