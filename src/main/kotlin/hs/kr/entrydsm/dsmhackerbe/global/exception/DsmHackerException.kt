package hs.kr.entrydsm.dsmhackerbe.global.exception

import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode

open class DsmHackerException(
    val errorCode: ErrorCode
) : RuntimeException(errorCode.message)
