package hackathon.sprinter.post.repository

import hackathon.sprinter.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<Post, Long>