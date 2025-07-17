package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "AI 문제 생성 요청")
data class GenerateProblemsRequest(
    @field:NotBlank(message = "사용자 ID는 필수입니다")
    @Schema(description = "AI 서버에 전달할 사용자 ID", example = "user123", required = true)
    val userId: String
)
