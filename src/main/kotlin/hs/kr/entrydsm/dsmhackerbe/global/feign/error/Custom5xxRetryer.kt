package hs.kr.entrydsm.dsmhackerbe.global.feign.config

import feign.RetryableException
import feign.Retryer

class Custom5xxRetryer(
    private val maxAttempts: Int = 2,
    private val backoff: Long = 100L
) : Retryer {

    private var attempt: Int = 1

    constructor() : this(2, 100L)

    override fun continueOrPropagate(e: RetryableException) {
        if (attempt++ >= maxAttempts) {
            throw e
        }

        try {
            Thread.sleep(backoff)
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    override fun clone(): Retryer {
        return Custom5xxRetryer(maxAttempts, backoff)
    }
}
