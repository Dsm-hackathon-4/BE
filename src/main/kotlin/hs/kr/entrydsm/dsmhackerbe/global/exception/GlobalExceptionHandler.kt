package hs.kr.entrydsm.dsmhackerbe.global.exception

import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DsmHackerException::class)
    fun handleDsmHackerException(
        exception: DsmHackerException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.of(exception.errorCode, request.requestURI, exception)
        return ResponseEntity.status(exception.errorCode.statusCode).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request.requestURI, exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
