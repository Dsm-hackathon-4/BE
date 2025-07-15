package hs.kr.entrydsm.dsmhackerbe.global.document.user

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.RegisterRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.UserLoginRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Tag(name = "User API", description = "사용자 관련 API")
interface UserApiDocument {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그인 성공"),
            ApiResponse(responseCode = "401", description = "인증 실패"),
            ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
        ]
    )
    fun login(@Valid @RequestBody request: UserLoginRequest): TokenResponse

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "회원가입 성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청"),
            ApiResponse(responseCode = "409", description = "이미 존재하는 사용자")
        ]
    )
    fun register(@Valid @RequestBody request: RegisterRequest)

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰")
        ]
    )
    fun reissue(@RequestHeader("X-Refresh-Token") token: String): TokenResponse

    @Operation(summary = "로그아웃", description = "현재 사용자를 로그아웃합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun logout()

    @Operation(summary = "회원탈퇴", description = "현재 사용자를 탈퇴시킵니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "회원탈퇴 성공"),
            ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
        ]
    )
    fun withdrawal()
}