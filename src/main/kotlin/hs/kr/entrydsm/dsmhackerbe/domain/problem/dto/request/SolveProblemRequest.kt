package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.request

import jakarta.validation.constraints.NotBlank

data class SolveProblemRequest(
    @field:NotBlank(message = "답변은 필수입니다")
    val answer: String
)
