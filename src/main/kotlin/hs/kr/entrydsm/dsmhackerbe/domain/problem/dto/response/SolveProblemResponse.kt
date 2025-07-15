package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

data class SolveProblemResponse(
    val isCorrect: Boolean,
    val xpEarned: Int,
    val explanation: String?,
    val xpBreakdown: XpBreakdown?
)

data class XpBreakdown(
    val baseXp: Int,
    val streakMultiplier: Double,
    val accuracyMultiplier: Double,
    val totalMultiplier: Double,
    val bonusXp: Int
)
