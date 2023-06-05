package hackathon.sprinter.configure

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost",
                "http://localhost:3000",
                "http://13.124.178.210",
                "http://13.124.178.210:9090",
            )
            .allowedMethods("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
    }
}
