package hs.kr.entrydsm.dsmhackerbe.global.exception.error

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val path: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun of(errorCode: ErrorCode, path: String, exception: Exception? = null): ErrorResponse {
            return ErrorResponse(
                status = errorCode.statusCode,
                message = errorCode.message,
                path = path
            )
        }
    }
}
