package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "문제 풀이 요청")
data class SolveProblemRequest(
    @field:NotBlank(message = "답변은 필수입니다")
    @Schema(title = "answer: String (사용자 답안)", description = "문제에 대한 사용자의 답안", example = "1", required = true)
    val answer: String
)
