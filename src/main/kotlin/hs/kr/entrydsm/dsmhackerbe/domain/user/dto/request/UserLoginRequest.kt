package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserLoginRequest(
    @field:Email
    val email: String,
    
    @field:NotBlank
    val password: String
)
