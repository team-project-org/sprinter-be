package hackathon.sprinter.jwt.filter

import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Bean
fun corsFilter(): CorsFilter {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.addAllowedOrigin("http://localhost:3000")
    config.addAllowedOrigin("http://13.124.178.210:3000")
    config.addAllowedHeader("*")
    config.addExposedHeader("Authorization")
    config.addExposedHeader("Authorization-refresh")
    config.addAllowedMethod("*")
    source.registerCorsConfiguration("/**", config)
    return CorsFilter(source)
}