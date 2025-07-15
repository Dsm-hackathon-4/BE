package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.XpBreakdown
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import org.springframework.stereotype.Service
import kotlin.math.ceil
import kotlin.math.min

@Service
class XpCalculationService(
    private val userProblemHistoryRepository: UserProblemHistoryRepository
) {
    
    companion object {
        // 기본 XP (난이도별)
        private val BASE_XP = mapOf(
            Difficulty.EASY to 10,
            Difficulty.MEDIUM to 20,
            Difficulty.HARD to 35
        )
        
        // 연속 정답 보너스 배율
        private val STREAK_MULTIPLIER = mapOf(
            5 to 1.1,   // 5문제 연속: 10% 보너스
            10 to 1.25, // 10문제 연속: 25% 보너스
            15 to 1.5,  // 15문제 연속: 50% 보너스
            20 to 1.75, // 20문제 연속: 75% 보너스
            25 to 2.0   // 25문제 연속: 100% 보너스
        )
        
        // 정답률 보너스 배율 (최근 10문제 기준)
        private val ACCURACY_MULTIPLIER = mapOf(
            90 to 1.3,  // 90% 이상: 30% 보너스
            80 to 1.2,  // 80% 이상: 20% 보너스
            70 to 1.1,  // 70% 이상: 10% 보너스
            60 to 1.0   // 60% 이상: 보너스 없음
        )
    }
    
    fun calculateXpWithBreakdown(user: User, problem: Problem, isCorrect: Boolean): XpResult {
        if (!isCorrect) {
            return XpResult(
                xpEarned = 0,
                breakdown = null
            )
        }
        
        val baseXp = BASE_XP[problem.difficulty] ?: 10
        val streakMultiplier = calculateStreakMultiplier(user)
        val accuracyMultiplier = calculateAccuracyMultiplier(user)
        
        val totalMultiplier = streakMultiplier * accuracyMultiplier
        val finalXp = ceil(baseXp * totalMultiplier).toInt()
        val bonusXp = finalXp - baseXp
        
        return XpResult(
            xpEarned = finalXp,
            breakdown = XpBreakdown(
                baseXp = baseXp,
                streakMultiplier = streakMultiplier,
                accuracyMultiplier = accuracyMultiplier,
                totalMultiplier = totalMultiplier,
                bonusXp = bonusXp
            )
        )
    }
    
    private fun calculateStreakMultiplier(user: User): Double {
        val currentStreak = getCurrentStreak(user)
        
        return STREAK_MULTIPLIER.entries
            .filter { currentStreak >= it.key }
            .maxByOrNull { it.key }
            ?.value ?: 1.0
    }
    
    private fun calculateAccuracyMultiplier(user: User): Double {
        val recentAccuracy = getRecentAccuracy(user)
        
        return ACCURACY_MULTIPLIER.entries
            .filter { recentAccuracy >= it.key }
            .maxByOrNull { it.key }
            ?.value ?: 0.9 // 60% 미만은 90%로 페널티
    }
    
    private fun getCurrentStreak(user: User): Int {
        val recentHistory = userProblemHistoryRepository
            .findByUserOrderBySolvedAtDesc(user)
            .take(50) // 최근 50개만 확인
        
        var streak = 0
        for (history in recentHistory) {
            if (history.isCorrect) {
                streak++
            } else {
                break
            }
        }
        return streak
    }
    
    private fun getRecentAccuracy(user: User): Int {
        val recentHistory = userProblemHistoryRepository
            .findByUserOrderBySolvedAtDesc(user)
            .take(10)
        
        if (recentHistory.isEmpty()) return 0
        
        val correctCount = recentHistory.count { it.isCorrect }
        return (correctCount * 100) / recentHistory.size
    }
    
    fun getStreakInfo(user: User): StreakInfo {
        val currentStreak = getCurrentStreak(user)
        val nextMilestone = STREAK_MULTIPLIER.keys.firstOrNull { it > currentStreak }
        val currentMultiplier = calculateStreakMultiplier(user)
        
        return StreakInfo(
            currentStreak = currentStreak,
            currentMultiplier = currentMultiplier,
            nextMilestone = nextMilestone,
            nextMultiplier = nextMilestone?.let { STREAK_MULTIPLIER[it] }
        )
    }
    
    fun getAccuracyInfo(user: User): AccuracyInfo {
        val recentAccuracy = getRecentAccuracy(user)
        val currentMultiplier = calculateAccuracyMultiplier(user)
        
        return AccuracyInfo(
            recentAccuracy = recentAccuracy,
            currentMultiplier = currentMultiplier,
            basedOnProblems = min(10, userProblemHistoryRepository.countByUser(user).toInt())
        )
    }
}

data class StreakInfo(
    val currentStreak: Int,
    val currentMultiplier: Double,
    val nextMilestone: Int?,
    val nextMultiplier: Double?
)

data class AccuracyInfo(
    val recentAccuracy: Int,
    val currentMultiplier: Double,
    val basedOnProblems: Int
)

data class XpResult(
    val xpEarned: Int,
    val breakdown: XpBreakdown?
)
