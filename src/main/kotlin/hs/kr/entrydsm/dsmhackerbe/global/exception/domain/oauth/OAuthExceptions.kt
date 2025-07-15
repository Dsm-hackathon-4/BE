package hs.kr.entrydsm.dsmhackerbe.global.exception.domain.oauth

import hs.kr.entrydsm.dsmhackerbe.global.exception.DsmHackerException
import hs.kr.entrydsm.dsmhackerbe.global.exception.error.ErrorCode

class UnsupportedProviderException : DsmHackerException(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER)

class InvalidKakaoOAuthResponseException : DsmHackerException(ErrorCode.INVALID_KAKAO_OAUTH_RESPONSE)

class InvalidKakaoAccessTokenException : DsmHackerException(ErrorCode.INVALID_KAKAO_ACCESS_TOKEN)
