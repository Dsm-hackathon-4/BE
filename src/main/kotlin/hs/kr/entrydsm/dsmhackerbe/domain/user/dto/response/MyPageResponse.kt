package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.Gender
import java.time.LocalDate

data class MyPageResponse(
    val userInfo: UserInfoResponse,
    val statistics: UserStatisticsResponse,
    val xpInfo: XpInfoResponse,
    val studyStreak: StudyStreakInfoResponse,
    val studyStatus: StudyStatusResponse,
    val goals: UserGoalsResponse
)

data class UserInfoResponse(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val gender: Gender,
    val age: String,
    val provider: String
)

data class UserStatisticsResponse(
    val totalXp: Int,
    val currentStreak: Int,
    val dailyRank: Int,
    val weeklyRank: Int,
    val monthlyRank: Int,
    val totalProblems: Int,
    val correctProblems: Int,
    val accuracy: Int
)

data class XpInfoResponse(
    val totalXp: Int,
    val todayXp: Int,
    val weeklyXp: Int,
    val monthlyXp: Int
)

data class StudyStreakInfoResponse(
    val currentStreak: Int,
    val maxStreak: Int,
    val totalStudyDays: Int,
    val lastStudyDate: LocalDate?
)

data class StudyStatusResponse(
    val totalProblems: Int,
    val correctProblems: Int,
    val reviewProblems: Int,
    val recentActivity: List<RecentActivityResponse>
)

data class RecentActivityResponse(
    val date: LocalDate,
    val problemCount: Int,
    val xpEarned: Int
)

data class UserGoalsResponse(
    val dailyGoal: Int,
    val todayProgress: Int,
    val achievementRate: Int,
    val streak: Int
)
