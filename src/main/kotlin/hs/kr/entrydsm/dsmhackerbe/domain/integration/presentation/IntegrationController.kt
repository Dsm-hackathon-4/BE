package hs.kr.entrydsm.dsmhackerbe.domain.integration.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request.SolveAiProblemRequest
import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.AiProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.SolveAiProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.service.AiProblemService
import hs.kr.entrydsm.dsmhackerbe.domain.integration.service.IntegrationService
import hs.kr.entrydsm.dsmhackerbe.global.document.integration.IntegrationApiDocument
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/integrations")
class IntegrationController(
    private val integrationService: IntegrationService,
    private val aiProblemService: AiProblemService
) : IntegrationApiDocument {
    
    @PostMapping("/generate-problems")
    override fun generateProblems(
        @Valid @RequestBody request: hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request.GenerateProblemsRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Map<String, String>> {
        val message = integrationService.generateProblemsFromIntegration(userDetails.username, request.userId)
        return ResponseEntity.ok(mapOf("message" to message))
    }
    
    @GetMapping("/batch-status")
    override fun getBatchStatus(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Map<String, Any?>> {
        val status = integrationService.getBatchStatus(userDetails.username)
        return ResponseEntity.ok(status)
    }
    
    @GetMapping("/ai-problems")
    override fun getAiProblems(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<Page<AiProblemResponse>> {
        val problems = aiProblemService.getAiProblems(userDetails.username, pageable)
        return ResponseEntity.ok(problems)
    }
    
    @GetMapping("/ai-problems/{problemId}")
    override fun getAiProblem(
        @PathVariable problemId: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<AiProblemResponse> {
        val problem = aiProblemService.getAiProblem(problemId, userDetails.username)
        return ResponseEntity.ok(problem)
    }
    
    @PostMapping("/ai-problems/{problemId}/solve")
    override fun solveAiProblem(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: SolveAiProblemRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<SolveAiProblemResponse> {
        val result = aiProblemService.solveAiProblem(problemId, request.answer, userDetails.username)
        return ResponseEntity.ok(result)
    }
}
