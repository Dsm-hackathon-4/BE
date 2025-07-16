package hs.kr.entrydsm.dsmhackerbe.global.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import hs.kr.entrydsm.dsmhackerbe.global.config.filter.FilterConfig
import hs.kr.entrydsm.dsmhackerbe.global.oauth.handler.OAuthFailureHandler
import hs.kr.entrydsm.dsmhackerbe.global.oauth.handler.OAuthSuccessHandler
import hs.kr.entrydsm.dsmhackerbe.global.security.auth.CustomOAuthUserDetailsService
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val objectMapper: ObjectMapper,
    private val jwtTokenProvider: JwtTokenProvider,
    private val oauthSuccessHandler: OAuthSuccessHandler,
    private val oauthFailureHandler: OAuthFailureHandler,
    private val customOAuthUserDetailsService: CustomOAuthUserDetailsService
) {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .cors { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .headers { 
                it.frameOptions { frameOptions -> frameOptions.deny() }
                  .httpStrictTransportSecurity { hstsConfig -> hstsConfig.disable() }
            }
            
            .exceptionHandling { exception ->
                exception
                    .authenticationEntryPoint { _, response, _ -> 
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized") 
                    }
                    .accessDeniedHandler { _, response, _ -> 
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied") 
                    }
            }
            
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(oauthSuccessHandler)
                    .failureHandler(oauthFailureHandler)
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuthUserDetailsService)
                    }
            }
            
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/oauth2/authorization/google/**", "/login/oauth2/code/google/**").permitAll()
                    .requestMatchers("/oauth2/authorization/kakao/**", "/login/oauth2/code/kakao/**").permitAll()
                    .requestMatchers("/users/login", "/users/register", "/users/reissue").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/oauth/success/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                    .anyRequest().authenticated()
            }
            
            .with(FilterConfig(jwtTokenProvider, objectMapper)) { }
            .build()
    }
}
