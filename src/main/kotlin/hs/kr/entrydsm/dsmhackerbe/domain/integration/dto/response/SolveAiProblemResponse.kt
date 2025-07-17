package hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "AI 문제 풀이 결과")
data class SolveAiProblemResponse(
    @Schema(description = "정답 여부", example = "true")
    val isCorrect: Boolean,
    
    @Schema(description = "정답", example = "LIFO")
    val correctAnswer: String,
    
    @Schema(description = "획득 XP", example = "10")
    val xpEarned: Int
)
