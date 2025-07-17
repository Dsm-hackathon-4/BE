package hs.kr.entrydsm.dsmhackerbe.global.document.integration

import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request.SolveAiProblemRequest
import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.AiProblemResponse
import hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.response.SolveAiProblemResponse
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

@Tag(name = "Integration API", description = "외부 서비스 연동 관련 API")
interface IntegrationApiDocument {

    @Operation(summary = "AI 문제 생성", description = "연동된 외부 데이터를 바탕으로 AI가 문제를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 생성 요청 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun generateProblems(
        @Valid @RequestBody request: hs.kr.entrydsm.dsmhackerbe.domain.integration.dto.request.GenerateProblemsRequest,
        userDetails: UserDetails
    ): ResponseEntity<Map<String, String>>

    @Operation(summary = "문제 생성 상태 조회", description = "문제 생성 배치 작업의 상태를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "상태 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getBatchStatus(userDetails: UserDetails): ResponseEntity<Map<String, Any?>>

    @Operation(summary = "AI 생성 문제 목록 조회", description = "사용자의 AI 생성 문제 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "AI 문제 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getAiProblems(
        userDetails: UserDetails,
        @Parameter(description = "페이지 정보") pageable: Pageable
    ): ResponseEntity<Page<AiProblemResponse>>

    @Operation(summary = "AI 생성 문제 상세 조회", description = "특정 AI 생성 문제의 상세 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "AI 문제 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun getAiProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        userDetails: UserDetails
    ): ResponseEntity<AiProblemResponse>

    @Operation(summary = "AI 생성 문제 풀이", description = "AI 생성 문제를 풀고 결과를 확인합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 풀이 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "접근 권한 없음"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun solveAiProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        @Valid @RequestBody request: SolveAiProblemRequest,
        userDetails: UserDetails
    ): ResponseEntity<SolveAiProblemResponse>
}
