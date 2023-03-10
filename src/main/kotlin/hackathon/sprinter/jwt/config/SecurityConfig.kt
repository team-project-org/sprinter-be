package hackathon.sprinter.jwt.config

import hackathon.sprinter.jwt.authenticationfilter.CustomJwtAuthenticationFilter
import hackathon.sprinter.jwt.authenticationfilter.CustomUsernamePasswordAuthenticationFilter
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
import org.springframework.web.filter.GenericFilterBean

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
                )
            it.ignoring()
                .antMatchers("/api/v1/signUp") // ???????????? ?????? ??????
                .antMatchers("/playground")
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // JWT ????????? ????????? ??????/?????? ????????? ??????????????? ?????? STATELESS ??????
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // ???????????? ????????? ????????? ????????? ??????
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic(Customizer.withDefaults())
            .addFilter(corsFilter())
            .addFilter(
                usernamePasswordAuthenticationFilter()
            )
            .addFilterBefore(
                jwtAuthenticationFilter(),
                BasicAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers("/graphql").permitAll()
            .antMatchers("/graphiql").permitAll()
            .anyRequest().permitAll()

            // ??????,?????? exception handling ??? ????????? ????????? ??????
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)

            // ???????????? ??? ????????? ???????????? ??????
            .and()
            .logout()
            .logoutSuccessUrl("/api/v1/login") // ??????????????? ????????? ???????????? "/"??? ??????

        return http.build()
    }

    /***
     * ????????? ??? authentication ?????? ??????
     */
    fun usernamePasswordAuthenticationFilter(): UsernamePasswordAuthenticationFilter? {
        return CustomUsernamePasswordAuthenticationFilter(
            authenticationManager(authenticationConfiguration),
            jwtProviderService
        ).apply { setFilterProcessesUrl("/api/v1/login") }
    }

    /***
     * JWT ?????? ??????
     */
    fun jwtAuthenticationFilter(): GenericFilterBean {
        return CustomJwtAuthenticationFilter(jwtProviderService)
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun authProvider() : DaoAuthenticationProvider {
        return DaoAuthenticationProvider()
            .also { it.setPasswordEncoder(passwordEncoder) }
            .also { it.setUserDetailsService(principalUserDetailsService) }
    }
}
