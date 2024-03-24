package hackathon.sprinter.github.service

import hackathon.sprinter.github.repository.GithubCacheRepository
import hackathon.sprinter.util.getHTMLFromGithubUrl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GithubQueryService(
    private val githubCacheRepository: GithubCacheRepository
) {

    companion object {
        private const val GITHUB_PAGE = "https://github.com/"
    }

    @Transactional
    fun getGithubPageHTML(username: String): String {
        val githubCache = githubCacheRepository.findByUsername(username)
        githubCache?.let {
            val yesterday = LocalDateTime.now().minusDays(1)

            if (yesterday.isBefore(it.dateUpdated)) {
                it.updateHtml(getHTMLFromGithubUrl(GITHUB_PAGE.plus(username)))
            }
            return it.html
        }
        return getHTMLFromGithubUrl(GITHUB_PAGE.plus(username))
    }
}