package hackathon.sprinter.util

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import java.time.Duration

@Component
class WebDriverUtil {
    companion object {
        private var WEB_DRIVER_PATH: String = "/Users/choi_keunyoung/IdeaProjects/sprinter-be/src/main/resources/chromedriver"
    }

    fun getWebDriver(): WebDriver {
        if (ObjectUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
            System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH)
        }
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--lang=ko")
        chromeOptions.addArguments("--no-sandbox")
        chromeOptions.addArguments("--disable-dev-shm-usage")
        chromeOptions.addArguments("--disable-gpu")
        chromeOptions.setCapability("ignoreProtectedModeSettings", true)

        val chromeDriver = ChromeDriver()
        chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return chromeDriver
    }

    fun quit(webDriver: WebDriver) {
        if (!ObjectUtils.isEmpty(webDriver)) {
            webDriver.quit()
        }
    }

    fun close(webDriver: WebDriver) {
        if (!ObjectUtils.isEmpty(webDriver)) {
            webDriver.close()
        }
    }
}