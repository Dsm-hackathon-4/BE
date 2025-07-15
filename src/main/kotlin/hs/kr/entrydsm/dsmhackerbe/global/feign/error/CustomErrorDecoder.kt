package hs.kr.entrydsm.dsmhackerbe.global.feign.error

import feign.Response
import feign.RetryableException
import feign.codec.ErrorDecoder

class CustomErrorDecoder : ErrorDecoder {

    private val defaultErrorDecoder = ErrorDecoder.Default()

    override fun decode(methodKey: String, response: Response): Exception {
        val exception = defaultErrorDecoder.decode(methodKey, response)

        if (response.status() >= 500) {
            return RetryableException(
                response.status(),
                exception.message,
                response.request().httpMethod(),
                exception,
                null as Long?,
                response.request()
            )
        }

        return exception
    }
}
