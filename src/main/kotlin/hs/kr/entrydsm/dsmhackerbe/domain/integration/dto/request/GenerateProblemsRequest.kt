package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AI 문제 생성 요청")
data class GenerateProblemsRequest(
    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val userId: String
)
