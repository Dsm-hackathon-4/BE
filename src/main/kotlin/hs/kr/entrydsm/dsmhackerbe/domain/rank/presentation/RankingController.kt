package hs.kr.entrydsm.dsmhackerbe.domain.rank.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.RankingResponse
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "Ranking", description = "랭킹 관련 API")
@RestController
@RequestMapping("/api/rankings")
class RankingController(
    private val rankingService: RankingService
) {

    @Operation(summary = "전체 랭킹 조회", description = "총 XP 기준 전체 랭킹을 조회합니다")
    @GetMapping
    fun getOverallRanking(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(defaultValue = "50") limit: Int
    ): ResponseEntity<RankingResponse> {
        return ResponseEntity.ok(
            rankingService.getOverallRanking(userDetails.username, limit)
        )
    }
}
