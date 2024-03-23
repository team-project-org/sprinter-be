package hackathon.sprinter.github.service

import hackathon.sprinter.util.getHTMLFromGithubUrl
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

@Service
class GithubQueryService {

    companion object {
        private const val GITHUB_PAGE = "https://github.com/"
    }
    fun getGithubPageHTML(username: String): String {
        return getHTMLFromGithubUrl(GITHUB_PAGE.plus(username))
    }
}