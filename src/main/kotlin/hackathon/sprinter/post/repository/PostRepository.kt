package hackathon.sprinter.post.repository

import hackathon.sprinter.post.model.Post
import hackathon.sprinter.post.model.QPost
import hackathon.sprinter.post.model.QPost.post
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PostRepository(
    private val postJpaRepository: PostJpaRepository
) : QuerydslRepositorySupport(Post::class.java), PostJpaRepository by postJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}