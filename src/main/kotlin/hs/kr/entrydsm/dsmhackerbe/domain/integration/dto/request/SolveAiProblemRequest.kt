package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "AI 문제 풀이 요청")
data class SolveAiProblemRequest(
    @field:NotBlank(message = "답변은 필수입니다")
    @Schema(description = "사용자 답안", example = "LIFO", required = true)
    val answer: String
)
