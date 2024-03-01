package hackathon.sprinter.util

fun getNotionPageId(url: String): String {
    return url.split("/").last()
        .replace(Regex("(.{8})(.{4})(.{4})(.{4})(.{12})"), "$1-$2-$3-$4-$5")
}