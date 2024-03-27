package hackathon.sprinter.linkedin.resolver

import hackathon.sprinter.linkedin.service.LinkedInQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LinkedInController(
    private val linkedInQueryService: LinkedInQueryService
) {
    @GetMapping("/linkedIn")
    fun getLinkedInHTML(@RequestParam url: String): Any {
        return linkedInQueryService.getLinkedInHTML(url)
    }

}