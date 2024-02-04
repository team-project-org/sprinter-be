package hackathon.sprinter.post.repository

import hackathon.sprinter.post.model.MockPost
import org.springframework.data.jpa.repository.JpaRepository

interface MockPostJpaRepository : JpaRepository<MockPost, Long> {
    fun findAllByDateDeletedIsNull(): List<MockPost>
}