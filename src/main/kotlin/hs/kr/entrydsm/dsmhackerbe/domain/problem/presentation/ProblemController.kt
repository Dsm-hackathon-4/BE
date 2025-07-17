package hs.kr.entrydsm.dsmhackerbe.domain.problem.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.request.SolveProblemRequest
import hs.kr.entrydsm.dsmhackerbe.domain.problem.dto.response.*
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Difficulty
import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.ProblemType
import hs.kr.entrydsm.dsmhackerbe.domain.problem.service.*
import hs.kr.entrydsm.dsmhackerbe.global.document.problem.ProblemApiDocument
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/problems")
class ProblemController(
    private val problemService: ProblemService,
    private val categoryService: CategoryService,
    private val problemSolveService: ProblemSolveService,
    private val userStatsService: UserStatsService,
    private val reviewService: ReviewService
) : ProblemApiDocument {

    @GetMapping("/categories")
    override fun getCategories(): List<CategoryResponse> {
        return categoryService.getAllCategories()
    }

    @GetMapping
    override fun getProblems(
        @RequestParam(required = false) categoryId: Long?,
        @RequestParam(required = false) difficulty: Difficulty?,
        @PageableDefault(size = 20) pageable: Pageable,
        @AuthenticationPrincipal userDetails: UserDetails
    ): Page<ProblemResponse> {
        return problemService.getProblems(categoryId, difficulty, pageable, userDetails.username)
    }

    @GetMapping("/{problemId}")
    override fun getProblem(
        @PathVariable problemId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ProblemResponse {
        return problemService.getProblem(problemId, userDetails.username)
    }

    @PostMapping("/{problemId}/solve")
    override fun solveProblem(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): SolveProblemResponse {
        return problemSolveService.solveProblem(problemId, request.answer, userDetails.username)
    }

    @GetMapping("/review")
    override fun getReviewProblems(
        @PageableDefault(size = 20) pageable: Pageable,
        @AuthenticationPrincipal userDetails: UserDetails
    ): Page<ReviewProblemResponse> {
        return reviewService.getReviewProblems(userDetails.username, pageable)
    }

    @GetMapping("/stats")
    override fun getUserStats(
        @AuthenticationPrincipal userDetails: UserDetails
    ): UserStatsResponse {
        return userStatsService.getUserStats(userDetails.username)
    }
    
    @GetMapping("/review/summary")
    override fun getReviewSummary(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<ReviewSummaryResponse> {
        val summary = reviewService.getReviewSummary(userDetails.username)
        return ResponseEntity.ok(summary)
    }

    // 추가 메서드들
    @GetMapping("/review/category")
    override fun getReviewProblemsByCategory(
        @RequestParam categoryName: String,
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<ReviewProblemResponse>> {
        return ResponseEntity.ok(
            reviewService.getReviewProblemsByCategory(userDetails.username, categoryName, pageable)
        )
    }

    @PostMapping("/review/{reviewId}/solve")
    override fun solveReviewProblem(
        @PathVariable reviewId: Long,
        @Valid @RequestBody request: SolveProblemRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<ReviewSolveResponse> {
        return ResponseEntity.ok(
            reviewService.solveReviewProblem(userDetails.username, reviewId, request.answer)
        )
    }
}
