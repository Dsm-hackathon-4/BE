package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.StudyStreak
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.StudyStreakRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
@Transactional
class StudyStreakService(
    private val studyStreakRepository: StudyStreakRepository,
    private val userRepository: UserRepository
) {
    
    fun recordStudyActivity(userEmail: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val today = LocalDate.now()
        var streak = studyStreakRepository.findByUserId(user.id!!)
        
        if (streak == null) {
            streak = StudyStreak(userId = user.id!!)
        }
        
        streak.checkAndResetStreak(today)
        streak.updateStreak(today)
        
        studyStreakRepository.save(streak)
    }
    
    @Transactional(readOnly = true)
    fun getStudyStreak(userEmail: String): StudyStreakInfo {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val streak = studyStreakRepository.findByUserId(user.id!!)
        
        return if (streak != null) {
            streak.checkAndResetStreak(LocalDate.now())
            StudyStreakInfo(
                currentStreak = streak.currentStreak,
                maxStreak = streak.maxStreak,
                totalStudyDays = streak.totalStudyDays,
                lastStudyDate = streak.lastStudyDate
            )
        } else {
            StudyStreakInfo(
                currentStreak = 0,
                maxStreak = 0,
                totalStudyDays = 0,
                lastStudyDate = null
            )
        }
    }
}

data class StudyStreakInfo(
    val currentStreak: Int,
    val maxStreak: Int,
    val totalStudyDays: Int,
    val lastStudyDate: LocalDate?
)
