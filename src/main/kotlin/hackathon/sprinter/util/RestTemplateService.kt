package hackathon.sprinter.util

import com.google.gson.Gson
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class RestTemplateService(
    private val gson: Gson,
) {
    companion object {
        private const val MOCK_MEMBER_URL = "https://pocketbase.remote.dongholab.com"
    }

    private val restTemplate = RestTemplate()

    fun <T> fetch(
        httpMethod: HttpMethod = HttpMethod.GET,
        url: String,
        type: Class<T>,
        httpHeaders: HttpHeaders? = null,
        body: String = "",
    ): T {
        val httpEntity =
            HttpEntity(body, httpHeaders ?: HttpHeaders().also { it.contentType = MediaType.APPLICATION_JSON })
        val response =
            restTemplate.exchange(
                URI.create("$MOCK_MEMBER_URL$url"),
                httpMethod,
                httpEntity,
                String::class.java
            ).body!!

        return gson.fromJson(response, type)
    }
}