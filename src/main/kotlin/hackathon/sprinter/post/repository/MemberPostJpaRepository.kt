package hackathon.sprinter.post.repository

import hackathon.sprinter.post.model.MemberPost
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPostJpaRepository : JpaRepository<MemberPost, Long>{
}