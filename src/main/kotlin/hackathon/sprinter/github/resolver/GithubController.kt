package hackathon.sprinter.github.resolver

import hackathon.sprinter.github.service.GithubQueryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GithubController(
    private val githubQueryService: GithubQueryService
) {
    @GetMapping("/test")
    fun getGithubPage(@RequestParam username: String) : Any {
        return githubQueryService.getGithubPageHTML(username);
    }
}