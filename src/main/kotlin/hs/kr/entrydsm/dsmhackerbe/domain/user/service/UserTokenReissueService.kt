package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.TokenResponse
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class UserTokenReissueService(
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun reissue(refreshToken: String): TokenResponse {
        return jwtTokenProvider.reissue(refreshToken)
    }
}
