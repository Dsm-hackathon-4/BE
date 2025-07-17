package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AI 문제 복습 요약 정보")
data class AiReviewSummaryResponse(
    @Schema(description = "새로 생성된 AI 문제 개수", example = "4")
    val newReviewCount: Int,
    
    @Schema(description = "진행 중인 AI 문제 개수", example = "7") 
    val ongoingReviewCount: Int,
    
    @Schema(description = "전체 AI 문제 개수", example = "11")
    val totalReviewCount: Int
)
