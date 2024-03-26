package hackathon.sprinter.linkedin.resolver

import hackathon.sprinter.linkedin.service.LinkedInQueryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class LinkedInController(
    private val linkedInQueryService: LinkedInQueryService
) {
    @GetMapping("/linkedIn")
    fun getLinkedInHTML(@RequestParam url: String): String {
        return linkedInQueryService.getLinkedInHTML(url)
    }

}