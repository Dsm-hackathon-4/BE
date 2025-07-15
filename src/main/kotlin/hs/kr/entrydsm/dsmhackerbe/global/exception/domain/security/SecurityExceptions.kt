package hs.kr.entrydsm.dsmhackerbe.global.exception.domain.security

import hs.kr.entrydsm.dsmhackerbe.global.exception.DsmHackerException
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode

class InvalidJwtException : DsmHackerException(ErrorCode.INVALID_TOKEN)

class ExpiredTokenException : DsmHackerException(ErrorCode.EXPIRED_TOKEN)
