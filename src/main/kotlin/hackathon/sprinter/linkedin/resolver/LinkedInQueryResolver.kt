package hackathon.sprinter.linkedin.resolver

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.linkedin.service.LinkedInQueryService

@DgsComponent
class LinkedInQueryResolver(
    private val linkedInQueryService: LinkedInQueryService
) {
    @DgsQuery
    fun getLinkedInHtml(@InputArgument(name = "linkedIn_url") linkedInUrl: String): String? {
        return linkedInQueryService.getLinkedInHTML(linkedInUrl)
    }
}