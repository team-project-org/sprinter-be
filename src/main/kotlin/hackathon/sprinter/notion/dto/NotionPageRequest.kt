package hackathon.sprinter.notion.dto

data class Cursor(
    val stack: List<Any>
)

data class NotionPageRequest(
    val pageId: String,
    val limit: Int,
    val cursor: Cursor,
    val chunkNumber: Int,
    val verticalColumns: Boolean
)