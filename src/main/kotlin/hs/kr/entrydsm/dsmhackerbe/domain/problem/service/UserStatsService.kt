package hs.kr.entrydsm.dsmhackerbe.domain.problem.service

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.StudyStreakResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.UserStatsResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.repository.UserProblemHistoryRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.StudyStreakService
import org.springframework.stereotype.Service

@Service
class UserStatsService(
    private val userRepository: UserRepository,
    private val userProblemHistoryRepository: UserProblemHistoryRepository,
    private val studyStreakService: StudyStreakService
) {
    
    fun getUserStats(userEmail: String): UserStatsResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val totalXp = userProblemHistoryRepository.getTotalXpByUser(user) ?: 0
        val totalProblems = userProblemHistoryRepository.countByUser(user).toInt()
        
        val allHistory = userProblemHistoryRepository.findByUserOrderBySolvedAtDesc(user)
        val correctProblems = allHistory.count { it.isCorrect }
        val overallAccuracy = if (totalProblems > 0) (correctProblems * 100) / totalProblems else 0
        
        // 학습 스트릭 정보 조회
        val streakInfo = studyStreakService.getStudyStreak(userEmail)
        val studyStreakResponse = StudyStreakResponse(
            currentStreak = streakInfo.currentStreak,
            maxStreak = streakInfo.maxStreak,
            totalStudyDays = streakInfo.totalStudyDays,
            lastStudyDate = streakInfo.lastStudyDate
        )
        
        // 임시로 더미 데이터
        val problemStreakInfo = StreakInfo(0, 1.0, 5, 1.1)
        val accuracyInfo = AccuracyInfo(0, 1.0, 0)
        
        return UserStatsResponse(
            totalXp = totalXp,
            totalProblems = totalProblems,
            correctProblems = correctProblems,
            overallAccuracy = overallAccuracy,
            streakInfo = problemStreakInfo,
            accuracyInfo = accuracyInfo,
            studyStreak = studyStreakResponse
        )
    }
}
