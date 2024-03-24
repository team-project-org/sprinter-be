package hackathon.sprinter.github.repository

import hackathon.sprinter.github.model.GithubCache
import org.springframework.data.jpa.repository.JpaRepository

interface GithubCacheRepository : JpaRepository<GithubCache, Long> {
    fun findByUsername(username: String) : GithubCache?
}