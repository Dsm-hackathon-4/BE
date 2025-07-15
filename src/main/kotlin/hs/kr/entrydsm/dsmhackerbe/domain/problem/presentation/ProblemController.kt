package hs.kr.entrydsm.dsmhackerbe.domain.problem.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.request.SolveProblemRequest
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.CategoryResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.ReviewSolveResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.SolveProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.CategoryService
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.ProblemService
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.UserStatsResponse
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.ProblemSolveService
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.ReviewService
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.UserStatsService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "Problem", description = "문제 관련 API")
@RestController
@RequestMapping("/api/problems")
class ProblemController(
    private val problemService: ProblemService,
    private val categoryService: CategoryService,
    private val problemSolveService: ProblemSolveService,
    private val userStatsService: UserStatsService,
    private val reviewService: ReviewService
) {

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리 목록을 조회합니다")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<CategoryResponse>> {
        return ResponseEntity.ok(categoryService.getAllCategories())
    }

    @Operation(summary = "문제 목록 조회", description = "조건에 따른 문제 목록을 조회합니다")
    @GetMapping
    fun getProblems(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) difficulty: Difficulty?,
        @RequestParam(required = false) type: ProblemType?,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<ProblemResponse>> {
        return ResponseEntity.ok(
            problemService.getProblems(category, difficulty, type, pageable)
        )
    }

    @Operation(summary = "문제 상세 조회", description = "특정 문제의 상세 정보를 조회합니다")
    @GetMapping("/{problemId}")
    fun getProblem(@PathVariable problemId: Long): ResponseEntity<ProblemResponse> {
        return ResponseEntity.ok(problemService.getProblemById(problemId))
    }

    @Operation(summary = "문제 풀이", description = "문제를 풀고 결과를 확인합니다")
    @PostMapping("/{problemId}/solve")
    fun solveProblem(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<SolveProblemResponse> {
        return ResponseEntity.ok(
            problemSolveService.solveProblem(problemId, request.answer, userDetails.username)
        )
    }

    @Operation(summary = "사용자 통계 조회", description = "사용자의 문제 풀이 통계를 조회합니다")
    @GetMapping("/stats")
    fun getUserStats(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<UserStatsResponse> {
        return ResponseEntity.ok(userStatsService.getUserStats(userDetails.username))
    }

    @Operation(summary = "복습 문제 목록", description = "사용자가 틀린 문제들의 복습 목록을 조회합니다")
    @GetMapping("/review")
    fun getReviewProblems(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<ReviewProblemResponse>> {
        return ResponseEntity.ok(
            reviewService.getReviewProblems(userDetails.username, pageable)
        )
    }

    @Operation(summary = "카테고리별 복습 문제 목록", description = "특정 카테고리의 복습 문제들을 조회합니다")
    @GetMapping("/review/category")
    fun getReviewProblemsByCategory(
        @RequestParam categoryName: String,
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<ReviewProblemResponse>> {
        return ResponseEntity.ok(
            reviewService.getReviewProblemsByCategory(userDetails.username, categoryName, pageable)
        )
    }

    @Operation(summary = "복습 문제 풀이", description = "복습 문제를 풀고 결과를 확인합니다")
    @PostMapping("/review/{reviewId}/solve")
    fun solveReviewProblem(
        @PathVariable reviewId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<ReviewSolveResponse> {
        return ResponseEntity.ok(
            reviewService.solveReviewProblem(userDetails.username, reviewId, request.answer)
        )
    }
}
