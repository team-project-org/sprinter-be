package hackathon.sprinter.github.resolver

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.github.service.GithubQueryService
import org.jsoup.nodes.Document

@DgsComponent
class GithubQueryResolver(
    private val githubQueryService: GithubQueryService
) {
    @DgsQuery
    fun getGithubPageHTML(@InputArgument(name = "username") username: String): String {
        return githubQueryService.getGithubPageHTML(username)
    }
}