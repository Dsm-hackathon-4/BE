package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max

data class SetGoalRequest(
    @field:Min(value = 1, message = "목표 문제 수는 최소 1개입니다")
    @field:Max(value = 100, message = "목표 문제 수는 최대 100개입니다")
    val dailyGoal: Int
)
