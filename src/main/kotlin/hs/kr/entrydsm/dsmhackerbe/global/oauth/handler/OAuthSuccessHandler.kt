package hs.kr.entrydsm.dsmhackerbe.global.oauth.handler

import hs.kr.entrydsm.dsmhackerbe.global.security.auth.AuthDetails
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class OAuthSuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val authDetails = authentication.principal as AuthDetails
        val tokenResponse = jwtTokenProvider.generateToken(
            authDetails.username,
            authDetails.authorities.first().authority.removePrefix("ROLE_")
        )

        val redirectUrl = UriComponentsBuilder
            .fromUriString("http://localhost:5173/selectSubject")
            .queryParam("accessToken", tokenResponse.accessToken)
            .queryParam("refreshToken", tokenResponse.refreshToken)
            .build().toUriString()

        response.sendRedirect(redirectUrl)
    }
}
