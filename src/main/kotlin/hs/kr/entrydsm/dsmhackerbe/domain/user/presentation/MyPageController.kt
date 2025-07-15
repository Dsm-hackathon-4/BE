package hs.kr.entrydsm.dsmhackerbe.domain.user.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.SetGoalRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.MyPageResponse
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.MyPageService
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.UserLogoutService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@Tag(name = "MyPage", description = "마이페이지 관련 API")
@RestController
@RequestMapping("/api/mypage")
class MyPageController(
    private val myPageService: MyPageService,
    private val userLogoutService: UserLogoutService
) {

    @Operation(summary = "마이페이지 조회", description = "사용자의 마이페이지 정보를 조회합니다")
    @GetMapping
    fun getMyPage(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MyPageResponse> {
        return ResponseEntity.ok(
            myPageService.getMyPage(userDetails.username)
        )
    }

    @Operation(summary = "일일 목표 설정", description = "사용자의 일일 문제 풀이 목표를 설정합니다")
    @PutMapping("/goal")
    fun setDailyGoal(
        @Valid @RequestBody request: SetGoalRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<Void> {
        myPageService.setDailyGoal(userDetails.username, request.dailyGoal)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리를 합니다")
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        userLogoutService.logout()
        return ResponseEntity.ok().build()
    }
}
