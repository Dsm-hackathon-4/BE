package hs.kr.entrydsm.dsmhackerbe.global.document.user

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.SetGoalRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.MyPageResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "MyPage API", description = "마이페이지 관련 API")
interface MyPageApiDocument {

    @Operation(summary = "마이페이지 조회", description = "사용자의 마이페이지 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마이페이지 조회 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun getMyPage(userDetails: UserDetails): ResponseEntity<MyPageResponse>

    @Operation(summary = "일일 목표 설정", description = "사용자의 일일 문제 풀이 목표를 설정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "일일 목표 설정 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (목표는 1~100개 사이)"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun setDailyGoal(
        @Valid @RequestBody request: SetGoalRequest,
        userDetails: UserDetails
    ): ResponseEntity<Void>

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리를 합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun logout(): ResponseEntity<Void>
}
