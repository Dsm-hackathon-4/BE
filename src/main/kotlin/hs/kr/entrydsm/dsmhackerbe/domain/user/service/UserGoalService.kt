package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.UserGoal
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserGoalRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserGoalService(
    private val userGoalRepository: UserGoalRepository,
    private val userRepository: UserRepository
) {
    
    fun addProblemProgress(userEmail: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        var goal = userGoalRepository.findByUserId(user.id!!)
        
        if (goal == null) {
            // 새 사용자의 경우 현재 풀이 + 3으로 목표 설정
            goal = UserGoal(userId = user.id!!, dailyGoal = 4) // 1 + 3
        } else {
            // 현재 풀이 기준 + 3으로 목표 자동 조정
            val newGoal = goal.todayProgress + 1 + 3 // 현재 진행도 + 이번 풀이 + 3
            goal.updateGoal(newGoal)
        }
        
        goal.addProgress()
        userGoalRepository.save(goal)
    }
    
    @Transactional(readOnly = true)
    fun getUserGoal(userEmail: String): UserGoal {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        val goal = userGoalRepository.findByUserId(user.id!!)
            ?: UserGoal(userId = user.id!!)
        
        goal.resetDailyProgressIfNeeded()
        return goal
    }
}
