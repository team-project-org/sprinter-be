package hackathon.sprinter.util

private val NOTION_PAGE_ID_REGEX = Regex("(.{8})(.{4})(.{4})(.{4})(.{12})")

fun getNotionPageId(url: String): String {
    val afterSlashUrl = url.substringAfterLast("/")

    if ("-" in afterSlashUrl) {
        return afterSlashUrl.substringAfterLast("-")
            .replace(NOTION_PAGE_ID_REGEX, "$1-$2-$3-$4-$5")
    }
    return afterSlashUrl
        .replace(NOTION_PAGE_ID_REGEX, "$1-$2-$3-$4-$5")
}