package hackathon.sprinter.linkedin.service

import hackathon.sprinter.util.WebDriverUtil
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class LinkedInQueryService(
    private val webDriverUtil: WebDriverUtil,
    private val redisTemplate: RedisTemplate<String, String>
) {
    companion object {
        private const val LINKEDIN_LOGIN_URL =
            "https://www.linkedin.com/login/ko?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin"
    }

    @Value("\${linkedin.username}")
    private lateinit var username : String

    @Value("\${linkedin.password}")
    private lateinit var password: String

    fun getLinkedInHTML(linkedInUrl: String): String {

        val opsForValue = redisTemplate.opsForValue()
        opsForValue.get(linkedInUrl)
            ?.let { return it }

        val webDriver = webDriverUtil.getWebDriver()

        loginLinkedIn(webDriver)
        webDriver.get(linkedInUrl)

        webDriver.pageSource
            .let {
                opsForValue.set(linkedInUrl, it, Duration.ofDays(1))
                webDriver.close()
                return it
            }
    }

    private fun loginLinkedIn(webDriver: WebDriver) {
        webDriver.get(LINKEDIN_LOGIN_URL);

        webDriver.findElement(By.id("username"))
            .sendKeys(username)
        webDriver.findElement(By.id("password"))
            .sendKeys(password)
        val loginButton =
            webDriver.findElement(By.cssSelector("#organic-div > form > div.login__form_action_container > button"))
        loginButton.click()
    }
}