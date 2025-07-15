package hs.kr.entrydsm.dsmhackerbe.global.security.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = jwtTokenProvider.resolveToken(request)
            println("JWT Token: $token")

            if (token != null && jwtTokenProvider.validateToken(token)) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
                println("인증 성공: ${authentication.name}")
            } else {
                println("JWT 토큰이 없거나 유효하지 않음")
            }
        } catch (e: Exception) {
            println("JWT 처리 중 오류: ${e.message}")
            e.printStackTrace()
        }
        
        filterChain.doFilter(request, response)
    }
}
