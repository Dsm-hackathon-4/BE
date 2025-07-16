package hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max

@Schema(description = "일일 목표 설정 요청")
data class SetGoalRequest(
    @field:Min(value = 1, message = "목표 문제 수는 최소 1개입니다")
    @field:Max(value = 100, message = "목표 문제 수는 최대 100개입니다")
    @JsonProperty("dailyGoal")
    @Schema(title = "dailyGoal: Int (일일 목표)", description = "하루 문제 풀이 목표 수", example = "10", minimum = "1", maximum = "100")
    val dailyGoal: Int = 0
)
