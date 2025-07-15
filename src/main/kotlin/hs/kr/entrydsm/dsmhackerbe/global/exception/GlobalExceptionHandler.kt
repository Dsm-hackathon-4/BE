package hs.kr.entrydsm.dsmhackerbe.global.exception

import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.slf4j.LoggerFactory

@RestControllerAdvice
class GlobalExceptionHandler {
    
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(DsmHackerException::class)
    fun handleDsmHackerException(
        exception: DsmHackerException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("DsmHackerException: ${exception.message}", exception)
        val errorResponse = ErrorResponse.of(exception.errorCode, request.requestURI, exception)
        return ResponseEntity.status(exception.errorCode.statusCode).body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("IllegalArgumentException: ${exception.message}", exception)
        val errorResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST, request.requestURI, exception)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected exception: ${exception.message}", exception)
        val errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request.requestURI, exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
