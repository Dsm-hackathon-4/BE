package hs.kr.entrydsm.dsmhackerbe.domain.rank.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.rank.dto.response.RankingResponse
import hs.kr.entrydsm.dsmhackerbe.domain.rank.service.RankingService
import hs.kr.entrydsm.dsmhackerbe.global.document.rank.RankingApiDocument
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rankings")
class RankingController(
    private val rankingService: RankingService
) : RankingApiDocument {

    @GetMapping
    override fun getRanking(
        @RequestParam(defaultValue = "50") limit: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RankingResponse {
        return rankingService.getOverallRanking(userDetails.username, limit)
    }

    @GetMapping("/daily")
    override fun getDailyRanking(
        @RequestParam(defaultValue = "50") limit: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RankingResponse {
        return rankingService.getDailyRanking(userDetails.username, limit)
    }

    @GetMapping("/weekly")
    override fun getWeeklyRanking(
        @RequestParam(defaultValue = "50") limit: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RankingResponse {
        return rankingService.getWeeklyRanking(userDetails.username, limit)
    }

    @GetMapping("/monthly")
    override fun getMonthlyRanking(
        @RequestParam(defaultValue = "50") limit: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RankingResponse {
        return rankingService.getMonthlyRanking(userDetails.username, limit)
    }
}
