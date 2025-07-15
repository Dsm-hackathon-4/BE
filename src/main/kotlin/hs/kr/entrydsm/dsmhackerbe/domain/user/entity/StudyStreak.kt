package hs.kr.entrydsm.dsmhackerbe.domain.user.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "tbl_study_streak")
class StudyStreak(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val userId: UUID,

    @Column(nullable = false)
    var currentStreak: Int = 0,

    @Column(nullable = false)
    var maxStreak: Int = 0,

    @Column(nullable = false)
    var lastStudyDate: LocalDate? = null,

    @Column(nullable = false)
    var totalStudyDays: Int = 0
) {
    fun updateStreak(studyDate: LocalDate) {
        when {
            lastStudyDate == null -> {
                // 첫 학습
                currentStreak = 1
                maxStreak = 1
                totalStudyDays = 1
            }
            lastStudyDate == studyDate -> {
                // 같은 날 중복 학습 - 스트릭 변화 없음
                return
            }
            lastStudyDate == studyDate.minusDays(1) -> {
                // 연속 학습
                currentStreak++
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                }
                totalStudyDays++
            }
            else -> {
                // 스트릭 끊김
                currentStreak = 1
                totalStudyDays++
            }
        }
        lastStudyDate = studyDate
    }
    
    fun checkAndResetStreak(today: LocalDate) {
        if (lastStudyDate != null && lastStudyDate!!.isBefore(today.minusDays(1))) {
            // 어제도 학습하지 않았으면 스트릭 리셋
            currentStreak = 0
        }
    }
}
