package hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.AccuracyInfo
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.StreakInfo
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.StudyStreakInfo
import java.time.LocalDate

data class UserStatsResponse(
    val totalXp: Int,
    val totalProblems: Int,
    val correctProblems: Int,
    val overallAccuracy: Int,
    val streakInfo: StreakInfo,
    val accuracyInfo: AccuracyInfo,
    val studyStreak: StudyStreakResponse
)

data class StudyStreakResponse(
    val currentStreak: Int,
    val maxStreak: Int,
    val totalStudyDays: Int,
    val lastStudyDate: LocalDate?
)
