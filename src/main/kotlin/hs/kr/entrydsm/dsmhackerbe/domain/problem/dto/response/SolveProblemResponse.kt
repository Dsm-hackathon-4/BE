package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "문제 풀이 결과")
data class SolveProblemResponse(
    @Schema(title = "isCorrect: Boolean (정답 여부)", description = "답안이 정답인지 여부", example = "true")
    val isCorrect: Boolean,
    
    @Schema(title = "correctAnswer: String (정답)", description = "문제의 정답", example = "TCP는 연결지향적이고 신뢰성을 보장합니다.", nullable = true)
    val correctAnswer: String?,
    
    @Schema(title = "xpEarned: Int (획득 XP)", description = "이번 문제로 획득한 경험치", example = "15")
    val xpEarned: Int,
    
    @Schema(title = "explanation: String (문제 해설)", description = "문제에 대한 상세 해설", example = "TCP는 연결지향적이고 신뢰성을 보장합니다.", nullable = true)
    val explanation: String?,
    
    @Schema(title = "xpBreakdown: XpBreakdown (XP 세부내역)", description = "XP 획득 상세 내역", nullable = true)
    val xpBreakdown: XpBreakdown?,
    
    @Schema(title = "chapterComplete: ChapterCompleteInfo (챕터 완료 정보)", description = "챕터 완료시에만 포함되는 통계", nullable = true)
    val chapterComplete: ChapterCompleteInfo?
)

@Schema(description = "챕터 완료 시 통계 정보")
data class ChapterCompleteInfo(
    @Schema(title = "isChapterCompleted: Boolean (챕터 완료 여부)", description = "이번 문제로 챕터가 완료되었는지", example = "true")
    val isChapterCompleted: Boolean,
    
    @Schema(title = "chapterTitle: String (챕터 제목)", description = "완료된 챕터 제목", example = "SQL 기초")
    val chapterTitle: String,
    
    @Schema(title = "totalXp: Int (챕터 총 XP)", description = "이 챕터에서 획득한 총 경험치", example = "180")
    val totalXp: Int,
    
    @Schema(title = "correctCount: Int (정답 수)", description = "이 챕터에서 맞춘 문제 수", example = "9")
    val correctCount: Int,
    
    @Schema(title = "totalCount: Int (전체 문제 수)", description = "이 챕터의 전체 문제 수 (항상 10)", example = "10")
    val totalCount: Int,
    
    @Schema(title = "accuracyRate: Int (정답률)", description = "이 챕터의 정답률 퍼센트", example = "90")
    val accuracyRate: Int
)

@Schema(description = "XP 획득 세부 내역")
data class XpBreakdown(
    @Schema(title = "baseXp: Int (기본 XP)", description = "문제의 기본 XP", example = "10")
    val baseXp: Int,
    
    @Schema(title = "streakMultiplier: Double (연속학습 배수)", description = "연속 학습에 따른 배수", example = "1.2")
    val streakMultiplier: Double,
    
    @Schema(title = "accuracyMultiplier: Double (정확도 배수)", description = "정확도에 따른 배수", example = "1.1")
    val accuracyMultiplier: Double,
    
    @Schema(title = "totalMultiplier: Double (총 배수)", description = "전체 적용된 배수", example = "1.32")
    val totalMultiplier: Double,
    
    @Schema(title = "bonusXp: Int (보너스 XP)", description = "추가 보너스 XP", example = "5")
    val bonusXp: Int
)
