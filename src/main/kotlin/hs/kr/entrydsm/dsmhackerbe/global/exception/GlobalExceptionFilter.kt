package hs.kr.entrydsm.dsmhackerbe.global.exception

import com.fasterxml.jackson.databind.ObjectMapper
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorResponse
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

class GlobalExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: DsmHackerException) {
            setErrorResponse(response, e.errorCode, request.requestURI)
        } catch (e: Exception) {
            setErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR, request.requestURI)
        }
    }

    private fun setErrorResponse(
        response: HttpServletResponse,
        errorCode: ErrorCode,
        path: String
    ) {
        response.status = errorCode.statusCode
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val errorResponse = ErrorResponse.of(errorCode, path)
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
