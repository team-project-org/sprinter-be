package hackathon.sprinter.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getHTMLFromGithubUrl(url: String): String {

    val document = Jsoup.connect(url).timeout(5000).get()
    return document.toString()
//    val parse = Jsoup.parse(toString)

//    return document
//    return document.getElementsByClass("application-main").toString()
}