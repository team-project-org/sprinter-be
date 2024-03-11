package hackathon.sprinter.notion.service

import hackathon.sprinter.notion.dto.Cursor
import hackathon.sprinter.notion.dto.NotionPageRequest
import hackathon.sprinter.notion.dto.RecordMap
import hackathon.sprinter.util.getNotionPageId
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class NotionQueryService(
    private val restTemplate: RestTemplate,
) {
    companion object {
        private const val NOTION_API_URL = "https://www.notion.so/api/v3/loadPageChunk"
    }

    fun getNotionPage(notionUrl: String): Any {
        val httpHeaders = HttpHeaders()
            .apply { contentType = MediaType.APPLICATION_JSON }

        val body = NotionPageRequest(
            pageId = getNotionPageId(notionUrl), limit = 50, cursor = Cursor(stack = emptyList()),
            chunkNumber = 0, verticalColumns = false
        )

        val httpEntity = HttpEntity<NotionPageRequest>(body, httpHeaders)
        val response: ResponseEntity<RecordMap> = restTemplate.exchange(NOTION_API_URL, HttpMethod.POST, httpEntity, RecordMap::class)
        if (response.statusCode === HttpStatus.OK) {
            return response.body!!.recordMap
        }
        throw RuntimeException()
    }
}