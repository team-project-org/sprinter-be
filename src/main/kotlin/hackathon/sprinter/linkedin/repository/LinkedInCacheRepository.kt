package hackathon.sprinter.linkedin.repository

import hackathon.sprinter.linkedin.model.LinkedInCache
import org.springframework.data.jpa.repository.JpaRepository

interface LinkedInCacheRepository : JpaRepository<LinkedInCache, Long> {
    fun findLinkedInCacheByProfileUrl(profileUrl: String) : LinkedInCache?
}