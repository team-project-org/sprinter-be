package hackathon.sprinter.notion.resolver

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import hackathon.sprinter.notion.service.NotionQueryService

@DgsComponent
class NotionQueryResolver(
    private val notionQueryService: NotionQueryService
) {
    @DgsQuery
    fun getNotionPage(@InputArgument(name = "notion_url") notionUrl: String) : Any {
        return notionQueryService.getNotionPage(notionUrl)
    }
}