package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "사용자 로그인 요청")
data class UserLoginRequest(
    @field:Email
    @Schema(title = "email: String (이메일)", description = "로그인할 이메일 주소", example = "user@example.com")
    val email: String,
    
    @field:NotBlank
    @Schema(title = "password: String (비밀번호)", description = "로그인 비밀번호", example = "password123")
    val password: String
)
