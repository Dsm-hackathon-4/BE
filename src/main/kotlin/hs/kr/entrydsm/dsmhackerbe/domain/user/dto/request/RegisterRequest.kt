package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:Email
    val email: String,

    @field:Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}\$")
    @field:Size(min = 8, max = 60)
    val password: String,

    @field:Size(min = 1, max = 30)
    val name: String,

    val profileImageUrl: String,

    val gender: Gender,

    val age: String
)
