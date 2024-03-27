package hackathon.sprinter.github.service

import hackathon.sprinter.util.getHTMLFromGithubUrl
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Service
@Transactional(readOnly = true)
class GithubQueryService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val GITHUB_PAGE = "https://github.com/"
    }

    @Transactional
    fun getGithubPageHTML(username: String): String {
        val githubProfileUrl = GITHUB_PAGE.plus(username)
        val opsForValue = redisTemplate.opsForValue()
        opsForValue.get(githubProfileUrl)
            ?.let { return it }

        getHTMLFromGithubUrl(githubProfileUrl)
            .let {
                opsForValue.set(githubProfileUrl, it, Duration.ofDays(1))
                return it
            }
    }
}