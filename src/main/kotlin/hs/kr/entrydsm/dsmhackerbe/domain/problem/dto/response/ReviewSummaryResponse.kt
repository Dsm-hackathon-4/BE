package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "복습 요약 정보")
data class ReviewSummaryResponse(
    @Schema(description = "새로 생성된 복습 개수", example = "4")
    val newReviewCount: Int,
    
    @Schema(description = "진행 중인 복습 개수", example = "7")
    val ongoingReviewCount: Int,
    
    @Schema(description = "전체 복습 개수", example = "11")
    val totalReviewCount: Int
)
