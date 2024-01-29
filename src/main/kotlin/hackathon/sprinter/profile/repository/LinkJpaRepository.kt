package hackathon.sprinter.profile.repository

import hackathon.sprinter.profile.model.Link
import org.springframework.data.jpa.repository.JpaRepository

interface LinkJpaRepository : JpaRepository<Link, Long>