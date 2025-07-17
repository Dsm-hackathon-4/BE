package hs.kr.entrydsm.dsmhackerbe.global.document.problem

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.request.SolveProblemRequest
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.*
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Problem API", description = "문제 관련 API")
interface ProblemApiDocument {

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getCategories(): List<CategoryResponse>

    @Operation(summary = "문제 목록 조회", description = "조건에 따른 문제 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getProblems(
        @Parameter(description = "카테고리 ID") @RequestParam(required = false) categoryId: Long?,
        @Parameter(description = "난이도") @RequestParam(required = false) difficulty: Difficulty?,
        @Parameter(description = "페이지 정보") pageable: Pageable,
        userDetails: UserDetails
    ): Page<ProblemResponse>

    @Operation(summary = "특정 문제 조회", description = "ID로 특정 문제의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun getProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        userDetails: UserDetails
    ): ProblemResponse

    @Operation(summary = "문제 풀이", description = "문제를 풀고 결과를 확인합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 풀이 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun solveProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        userDetails: UserDetails
    ): SolveProblemResponse

    @Operation(summary = "복습 문제 목록", description = "틀린 문제들의 복습 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "복습 문제 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getReviewProblems(
        @Parameter(description = "페이지 정보") pageable: Pageable,
        userDetails: UserDetails
    ): Page<ReviewProblemResponse>

    @Operation(summary = "사용자 통계", description = "사용자의 문제 풀이 통계를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용자 통계 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getUserStats(userDetails: UserDetails): UserStatsResponse

    @Operation(summary = "복습 요약 정보 조회", description = "새로 생성된 복습과 진행 중인 복습의 개수를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "복습 요약 정보 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getReviewSummary(userDetails: UserDetails): ResponseEntity<hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewSummaryResponse>

    @Operation(summary = "카테고리별 복습 문제 목록", description = "특정 카테고리의 복습 문제들을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리별 복습 문제 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "카테고리를 찾을 수 없음")
        ]
    )
    fun getReviewProblemsByCategory(
        @Parameter(description = "카테고리 이름") @RequestParam categoryName: String,
        userDetails: UserDetails,
        @Parameter(description = "페이지 정보") pageable: Pageable
    ): ResponseEntity<Page<ReviewProblemResponse>>

    @Operation(summary = "복습 문제 풀이", description = "복습 문제를 풀고 결과를 확인합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "복습 문제 풀이 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "404", description = "복습 문제를 찾을 수 없음")
        ]
    )
    fun solveReviewProblem(
        @Parameter(description = "복습 ID") @PathVariable reviewId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        userDetails: UserDetails
    ): ResponseEntity<ReviewSolveResponse>
}
