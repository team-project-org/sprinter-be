package hackathon.sprinter.util

import org.jsoup.Jsoup

fun getHTMLFromGithubUrl(url: String): String {
    val document = Jsoup.connect(url).timeout(5000).get()
    return document.toString()
}