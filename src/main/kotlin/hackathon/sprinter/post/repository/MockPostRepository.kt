package hackathon.sprinter.post.repository

import hackathon.sprinter.post.model.MockPost
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class MockPostRepository(
    private val mockPostJpaRepository: MockPostJpaRepository
) : QuerydslRepositorySupport(MockPost::class.java), MockPostJpaRepository by mockPostJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}