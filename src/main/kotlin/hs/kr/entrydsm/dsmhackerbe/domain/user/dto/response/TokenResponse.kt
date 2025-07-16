package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "토큰 응답")
data class TokenResponse(
    @Schema(title = "accessToken: String (액세스 토큰)", description = "API 인증용 액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    val accessToken: String,
    
    @Schema(title = "refreshToken: String (리프레시 토큰)", description = "토큰 갱신용 리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9...")
    val refreshToken: String
)
