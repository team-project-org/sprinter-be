package hackathon.sprinter.jwt.config

import hackathon.sprinter.jwt.authenticationfilter.CustomJwtAuthorizationFilter
import hackathon.sprinter.jwt.authenticationfilter.CustomUsernamePasswordAuthenticationFilter
import hackathon.sprinter.jwt.authenticationfilter.JwtAuthorizationExceptionFilter
import hackathon.sprinter.jwt.filter.corsFilter
import hackathon.sprinter.jwt.handler.CustomAccessDeniedHandler
import hackathon.sprinter.jwt.handler.CustomAuthenticationEntryPoint
import hackathon.sprinter.jwt.service.JwtProviderService
import hackathon.sprinter.jwt.service.PrincipalUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val passwordEncoder: BCryptPasswordEncoder,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtProviderService: JwtProviderService,
    private val principalUserDetailsService: PrincipalUserDetailsService,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler,
) {
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring()
                .antMatchers(
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/swagger/**",
                    "/webjars/**",
                    "/api/v1/sign-up",
                    "/hello",
                    "/playground",
                )
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // JWT 방식의 무상태 인증/인가 처리를 적용하므로 세션 STATELESS 처리
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // 비활성화 필터와 커스텀 필터를 추가
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic(Customizer.withDefaults())
            .addFilter(corsFilter())
            .addFilter(usernamePasswordAuthenticationFilter())
            .addFilterBefore(CustomJwtAuthorizationFilter(jwtProviderService), BasicAuthenticationFilter::class.java)
            .addFilterBefore(JwtAuthorizationExceptionFilter(jwtProviderService), CustomJwtAuthorizationFilter::class.java)
            .authorizeRequests()
            .antMatchers("/graphql").permitAll()
            .antMatchers("/graphiql").permitAll()
            .anyRequest().permitAll()

            // 인증,인가 exception handling 시 커스텀 파일로 처리
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)

            // 로그아웃 시 로그인 페이지로 이동
            .and()
            .logout()
            .logoutUrl("/api/v1/logout")
            .logoutSuccessUrl("/") // 로그아웃에 대해서 성공하면 "/"로 이동

        return http.build()
    }

    /***
     * 로그인 시 authentication 필터 적용
     */
    fun usernamePasswordAuthenticationFilter(): UsernamePasswordAuthenticationFilter? {
        return CustomUsernamePasswordAuthenticationFilter(
            authenticationManager(authenticationConfiguration),
            jwtProviderService
        ).apply { setFilterProcessesUrl("/api/v1/login") }
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun authProvider(): DaoAuthenticationProvider {
        return DaoAuthenticationProvider()
            .also { it.setPasswordEncoder(passwordEncoder) }
            .also { it.setUserDetailsService(principalUserDetailsService) }
    }

}