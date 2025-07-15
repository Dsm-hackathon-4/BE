package hs.kr.entrydsm.dsmhackerbe.domain.user.repository

import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.UserGoal
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserGoalRepository : JpaRepository<UserGoal, Long> {
    fun findByUserId(userId: UUID): UserGoal?
}
