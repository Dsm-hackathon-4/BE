package hs.kr.entrydsm.dsmhackerbe.global.document.admin

import hs.kr.entrydsm.dsmhackerbe.domain.problem.entity.Problem
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "Admin API", description = "관리자 전용 API")
interface AdminApiDocument {

    @Operation(summary = "사용자 목록 조회", description = "모든 사용자 목록을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요")
        ]
    )
    fun getUsers(
        @Parameter(description = "페이지 정보") pageable: Pageable,
        adminDetails: UserDetails
    ): Page<User>

    @Operation(summary = "사용자 삭제", description = "특정 사용자를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "사용자 삭제 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
        ]
    )
    fun deleteUser(
        @Parameter(description = "사용자 ID") @PathVariable userId: String,
        adminDetails: UserDetails
    ): ResponseEntity<Void>

    @Operation(summary = "문제 생성", description = "새로운 문제를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "문제 생성 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요")
        ]
    )
    fun createProblem(
        @RequestBody problemRequest: Any, // 실제 구현 시 CreateProblemRequest DTO 생성 필요
        adminDetails: UserDetails
    ): Problem

    @Operation(summary = "문제 수정", description = "기존 문제를 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "문제 수정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun updateProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        @RequestBody problemRequest: Any, // 실제 구현 시 UpdateProblemRequest DTO 생성 필요
        adminDetails: UserDetails
    ): Problem

    @Operation(summary = "문제 삭제", description = "기존 문제를 삭제합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "문제 삭제 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            ApiResponse(responseCode = "404", description = "문제를 찾을 수 없음")
        ]
    )
    fun deleteProblem(
        @Parameter(description = "문제 ID") @PathVariable problemId: Long,
        adminDetails: UserDetails
    ): ResponseEntity<Void>

    @Operation(summary = "시스템 통계", description = "시스템 전체 통계를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "시스템 통계 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            ApiResponse(responseCode = "403", description = "관리자 권한 필요")
        ]
    )
    fun getSystemStats(adminDetails: UserDetails): Any // 실제 구현 시 SystemStatsResponse DTO 생성 필요
}
