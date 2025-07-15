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
    
    fun setDailyGoal(userEmail: String, dailyGoal: Int) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        var goal = userGoalRepository.findByUserId(user.id!!)
        
        if (goal == null) {
            goal = UserGoal(userId = user.id!!, dailyGoal = dailyGoal)
        } else {
            goal.updateGoal(dailyGoal)
        }
        
        userGoalRepository.save(goal)
    }
    
    fun addProblemProgress(userEmail: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다")
        
        var goal = userGoalRepository.findByUserId(user.id!!)
        
        if (goal == null) {
            goal = UserGoal(userId = user.id!!)
            userGoalRepository.save(goal)
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
