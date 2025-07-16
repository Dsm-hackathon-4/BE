package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.ChapterResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapDetailResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.service.RoadmapService
import hs.kr.entrydsm.dsmhackerbe.global.document.roadmap.RoadmapApiDocument
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/roadmaps")
class RoadmapController(
    private val roadmapService: RoadmapService
) : RoadmapApiDocument {

    @GetMapping
    override fun getAllRoadmaps(
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<RoadmapResponse> {
        return roadmapService.getAllRoadmaps(userDetails.username)
    }

    @GetMapping("/category/{categoryId}")
    override fun getRoadmapsByCategory(
        @PathVariable categoryId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<RoadmapResponse> {
        return roadmapService.getRoadmapsByCategory(categoryId, userDetails.username)
    }

    @GetMapping("/{roadmapId}")
    override fun getRoadmapDetail(
        @PathVariable roadmapId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): RoadmapDetailResponse {
        return roadmapService.getRoadmapDetail(roadmapId, userDetails.username)
    }

    @GetMapping("/{roadmapId}/chapters")
    override fun getRoadmapChapters(
        @PathVariable roadmapId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<ChapterResponse> {
        return roadmapService.getRoadmapChapters(roadmapId, userDetails.username)
    }

    @GetMapping("/{roadmapId}/chapters/{chapterId}/problems")
    override fun getChapterProblems(
        @PathVariable roadmapId: Long,
        @PathVariable chapterId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): List<hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse> {
        return roadmapService.getChapterProblems(chapterId, userDetails.username)
    }
}
