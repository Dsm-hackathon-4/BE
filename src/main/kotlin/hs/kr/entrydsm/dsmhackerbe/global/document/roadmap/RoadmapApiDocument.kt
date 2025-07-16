package hs.kr.entrydsm.dsmhackerbe.global.document.roadmap

import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.ChapterResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapDetailResponse
import hs.kr.entrydsm.dsmhackerbe.domain.roadmap.dto.response.RoadmapResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "Roadmap API", description = "로드맵 관련 API")
interface RoadmapApiDocument {

    @Operation(summary = "전체 로드맵 조회", description = "모든 활성화된 로드맵 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로드맵 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getAllRoadmaps(userDetails: UserDetails): List<RoadmapResponse>

    @Operation(summary = "카테고리별 로드맵 조회", description = "특정 카테고리의 로드맵 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리별 로드맵 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
        ]
    )
    fun getRoadmapsByCategory(
        @Parameter(description = "카테고리 ID") @PathVariable categoryId: Long,
        userDetails: UserDetails
    ): List<RoadmapResponse>

    @Operation(summary = "로드맵 상세 조회", description = "특정 로드맵의 상세 정보와 문제 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로드맵 상세 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "로드맵을 찾을 수 없음")
        ]
    )
    fun getRoadmapDetail(
        @Parameter(description = "로드맵 ID") @PathVariable roadmapId: Long,
        userDetails: UserDetails
    ): RoadmapDetailResponse

    @Operation(summary = "로드맵 챕터 목록 조회", description = "특정 로드맵의 모든 챕터 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "챕터 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "로드맵을 찾을 수 없음")
        ]
    )
    fun getRoadmapChapters(
        @Parameter(description = "로드맵 ID") @PathVariable roadmapId: Long,
        userDetails: UserDetails
    ): List<ChapterResponse>

    @Operation(summary = "챕터 문제 목록 조회", description = "특정 챕터의 모든 문제 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "챕터 문제 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "챕터를 찾을 수 없음")
        ]
    )
    fun getChapterProblems(
        @Parameter(description = "로드맵 ID") @PathVariable roadmapId: Long,
        @Parameter(description = "챕터 ID") @PathVariable chapterId: Long,
        userDetails: UserDetails
    ): List<hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse>
}
