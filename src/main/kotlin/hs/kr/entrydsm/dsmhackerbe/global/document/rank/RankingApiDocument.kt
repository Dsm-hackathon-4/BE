package hs.kr.entrydsm.dsmhackerbe.global.document.rank

import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.RankingResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Ranking API", description = "랭킹 관련 API")
interface RankingApiDocument {

    @Operation(summary = "전체 랭킹 조회", description = "전체 사용자의 XP 기준 랭킹을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "랭킹 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getRanking(
        @Parameter(description = "조회할 랭킹 수 (기본값: 50)") @RequestParam(defaultValue = "50") limit: Int,
        userDetails: UserDetails
    ): RankingResponse

    @Operation(summary = "일일 랭킹 조회", description = "오늘의 XP 기준 랭킹을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "일일 랭킹 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getDailyRanking(
        @Parameter(description = "조회할 랭킹 수 (기본값: 50)") @RequestParam(defaultValue = "50") limit: Int,
        userDetails: UserDetails
    ): RankingResponse

    @Operation(summary = "주간 랭킹 조회", description = "이번 주의 XP 기준 랭킹을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "주간 랭킹 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getWeeklyRanking(
        @Parameter(description = "조회할 랭킹 수 (기본값: 50)") @RequestParam(defaultValue = "50") limit: Int,
        userDetails: UserDetails
    ): RankingResponse

    @Operation(summary = "월간 랭킹 조회", description = "이번 달의 XP 기준 랭킹을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "월간 랭킹 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getMonthlyRanking(
        @Parameter(description = "조회할 랭킹 수 (기본값: 50)") @RequestParam(defaultValue = "50") limit: Int,
        userDetails: UserDetails
    ): RankingResponse
}
