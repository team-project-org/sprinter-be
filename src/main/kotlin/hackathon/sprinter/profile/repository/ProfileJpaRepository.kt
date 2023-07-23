package hackathon.sprinter.profile.repository

import hackathon.sprinter.profile.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileJpaRepository : JpaRepository<Profile, Long> {
    fun findByMemberId(memberId: Long): Profile
}