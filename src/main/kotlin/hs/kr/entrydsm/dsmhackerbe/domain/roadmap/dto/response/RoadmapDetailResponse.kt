package hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로드맵 상세 정보")
data class RoadmapDetailResponse(
    @Schema(title = "roadmap: RoadmapResponse (로드맵 정보)", description = "로드맵 기본 정보")
    val roadmap: RoadmapResponse,
    
    @Schema(title = "problems: RoadmapProblems (문제 목록)", description = "난이도별 문제 목록")
    val problems: RoadmapProblems
)

@Schema(description = "로드맵 문제 목록 (난이도별)")
data class RoadmapProblems(
    @Schema(title = "easy: List<ProblemResponse> (쉬움 문제)", description = "Easy 난이도 문제들")
    val easy: List<ProblemResponse>,
    
    @Schema(title = "medium: List<ProblemResponse> (보통 문제)", description = "Medium 난이도 문제들")
    val medium: List<ProblemResponse>,
    
    @Schema(title = "hard: List<ProblemResponse> (어려움 문제)", description = "Hard 난이도 문제들")
    val hard: List<ProblemResponse>
)
