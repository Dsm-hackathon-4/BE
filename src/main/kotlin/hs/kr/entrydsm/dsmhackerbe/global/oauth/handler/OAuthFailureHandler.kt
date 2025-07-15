package hs.kr.entrydsm.dsmhackerbe.global.oauth.handler

import com.fasterxml.jackson.databind.ObjectMapper
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

@Component
class OAuthFailureHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val errorCode = ErrorCode.OAUTH_AUTHENTICATION_FAILED
        val errorResponse = ErrorResponse.of(errorCode, request.requestURI, exception)

        response.status = errorCode.statusCode
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
        response.writer.flush()
    }
}
