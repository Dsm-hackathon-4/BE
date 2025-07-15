package hs.kr.entrydsm.dsmhackerbe.global.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import hs.kr.entrydsm.dsmhackerbe.global.exception.GlobalExceptionFilter
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenFilter
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenProvider
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class FilterConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val objectMapper: ObjectMapper
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(http: HttpSecurity) {
        val jwtTokenFilter = JwtTokenFilter(jwtTokenProvider)
        val globalExceptionFilter = GlobalExceptionFilter(objectMapper)

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(globalExceptionFilter, JwtTokenFilter::class.java)
    }
}
