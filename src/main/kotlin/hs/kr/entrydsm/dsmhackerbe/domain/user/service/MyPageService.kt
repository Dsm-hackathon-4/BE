package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.repository.UserRankingRepository
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.*
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.UserGoal
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserGoalRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Service
@Transactional(readOnly = true)
class MyPageService(
    private val userRepository: UserRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository,
    private val userRankingRepository: UserRankingRepository,
    private val userGoalRepository: UserGoalRepository,
    private val studyStreakService: StudyStreakService,
    private val userGoalService: UserGoalService
) {
    
    fun getMyPage(userEmail: String): MyPageResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        // 기본 사용자 정보
        val userInfo = UserInfoResponse(
            id = user.id.toString(),
            name = user.name,
            email = user.email,
            profileImageUrl = user.profileImageUrl,
            gender = user.gender,
            age = user.age,
            provider = user.provider
        )
        
        // 통계 정보
        val statistics = getUserStatistics(user.id!!, userEmail)
        
        // XP 정보
        val xpInfo = getXpInfo(user.id!!)
        
        // 학습 스트릭 정보
        val studyStreakInfo = getStudyStreakInfo(userEmail)
        
        // 학습 현황
        val studyStatus = getStudyStatus(user.id!!)
        
        // 목표 정보
        val goals = getUserGoals(user.id!!)
        
        return MyPageResponse(
            userInfo = userInfo,
            statistics = statistics,
            xpInfo = xpInfo,
            studyStreak = studyStreakInfo,
            studyStatus = studyStatus,
            goals = goals
        )
    }
    
    private fun getUserStatistics(userId: java.util.UUID, userEmail: String): UserStatisticsResponse {
        val user = userRepository.findById(userId).get()
        val totalXp = user.totalXp
        
        val studyStreak = studyStreakService.getStudyStreak(userEmail)
        
        // 실제 랭킹 계산 - 총 XP 기준
        val allUsers = userRepository.findAll()
        val myRank = allUsers.count { otherUser ->
            otherUser.totalXp > user.totalXp
        } + 1
        
        val allHistory = userProblemHistoryRepository.findByUserOrderBySolvedAtDesc(user)
        val totalProblems = allHistory.size
        val correctProblems = allHistory.count { it.isCorrect }
        val accuracy = if (totalProblems > 0) (correctProblems * 100) / totalProblems else 0
        
        return UserStatisticsResponse(
            totalXp = totalXp,
            currentStreak = studyStreak.currentStreak,
            dailyRank = myRank,
            weeklyRank = myRank,
            monthlyRank = myRank,
            totalProblems = totalProblems,
            correctProblems = correctProblems,
            accuracy = accuracy
        )
    }
    
    private fun getXpInfo(userId: java.util.UUID): XpInfoResponse {
        val user = userRepository.findById(userId).get()
        val totalXp = user.totalXp
        
        val today = LocalDate.now()
        val weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val monthStart = today.withDayOfMonth(1)
        
        val todayRanking = userRankingRepository.findByUserIdAndRankingDate(userId, today)
        val todayXp = todayRanking?.dailyXp ?: 0
        
        val weeklyXp = userRankingRepository.getUserWeeklyXp(userId, weekStart) ?: 0
        val monthlyXp = userRankingRepository.getUserMonthlyXp(userId, monthStart) ?: 0
        
        return XpInfoResponse(
            totalXp = totalXp,
            todayXp = todayXp,
            weeklyXp = weeklyXp,
            monthlyXp = monthlyXp
        )
    }
    
    private fun getStudyStreakInfo(userEmail: String): StudyStreakInfoResponse {
        val streakInfo = studyStreakService.getStudyStreak(userEmail)
        
        return StudyStreakInfoResponse(
            currentStreak = streakInfo.currentStreak,
            maxStreak = streakInfo.maxStreak,
            totalStudyDays = streakInfo.totalStudyDays,
            lastStudyDate = streakInfo.lastStudyDate
        )
    }
    
    private fun getStudyStatus(userId: java.util.UUID): StudyStatusResponse {
        val user = userRepository.findById(userId).get()
        val allHistory = userProblemHistoryRepository.findByUserOrderBySolvedAtDesc(user)
        
        val totalProblems = allHistory.size
        val correctProblems = allHistory.count { it.isCorrect }
        val reviewProblems = 0 // 복습 문제 수 (ReviewService에서 가져와야 함)
        
        // 최근 7일간 활동
        val recentActivity = (0..6).map { daysAgo ->
            val date = LocalDate.now().minusDays(daysAgo.toLong())
            val dayHistory = allHistory.filter { 
                it.solvedAt.toLocalDate() == date 
            }
            RecentActivityResponse(
                date = date,
                problemCount = dayHistory.size,
                xpEarned = dayHistory.sumOf { it.xpEarned }
            )
        }.reversed()
        
        return StudyStatusResponse(
            totalProblems = totalProblems,
            correctProblems = correctProblems,
            reviewProblems = reviewProblems,
            recentActivity = recentActivity
        )
    }
    
    private fun getUserGoals(userId: java.util.UUID): UserGoalsResponse {
        val user = userRepository.findById(userId).get()
        val goal = userGoalService.getUserGoal(user.email)
        
        return UserGoalsResponse(
            dailyGoal = goal.dailyGoal,
            todayProgress = goal.todayProgress,
            achievementRate = goal.getAchievementRate(),
            streak = goal.currentStreak
        )
    }
}
