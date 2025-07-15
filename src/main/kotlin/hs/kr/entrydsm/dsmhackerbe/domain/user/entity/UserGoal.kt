package hs.kr.entrydsm.dsmhackerbe.domain.user.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "tbl_user_goal")
class UserGoal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val userId: UUID,

    @Column(nullable = false)
    var dailyGoal: Int = 5,

    @Column(nullable = false)
    var todayProgress: Int = 0,

    @Column(nullable = false)
    var lastUpdatedDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    var currentStreak: Int = 0,

    @Column(nullable = false)
    var maxStreak: Int = 0,

    @Column(nullable = false)
    var totalAchievements: Int = 0
) {
    fun updateGoal(newGoal: Int) {
        this.dailyGoal = newGoal
    }
    
    fun addProgress() {
        val today = LocalDate.now()
        
        // 날짜가 바뀌었으면 진행도 초기화
        if (lastUpdatedDate != today) {
            // 어제 목표를 달성했는지 확인
            if (lastUpdatedDate == today.minusDays(1) && todayProgress >= dailyGoal) {
                currentStreak++
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak
                }
                totalAchievements++
            } else if (lastUpdatedDate.isBefore(today.minusDays(1))) {
                // 하루 이상 건너뛰었으면 스트릭 리셋
                currentStreak = 0
            }
            
            todayProgress = 0
            lastUpdatedDate = today
        }
        
        todayProgress++
        
        // 오늘 목표 달성시 처리
        if (todayProgress == dailyGoal) {
            // 스트릭은 다음날에 체크됨
        }
    }
    
    fun getAchievementRate(): Int {
        return if (dailyGoal > 0) {
            ((todayProgress.toDouble() / dailyGoal) * 100).toInt().coerceAtMost(100)
        } else 0
    }
    
    fun resetDailyProgressIfNeeded() {
        val today = LocalDate.now()
        if (lastUpdatedDate != today) {
            addProgress() // 날짜 체크 및 진행도 리셋은 addProgress에서 처리
            todayProgress = 0 // 실제 진행도는 0으로 리셋
        }
    }
}
