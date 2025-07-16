package hs.kr.entrydsm.dsmhackerbe.domain.user.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.SetGoalRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.MyPageResponse
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.MyPageService
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.UserLogoutService
import hs.kr.entrydsm.dsmhackerbe.global.document.user.MyPageApiDocument
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/mypage")
class MyPageController(
    private val myPageService: MyPageService,
    private val userLogoutService: UserLogoutService
) : MyPageApiDocument {

    @GetMapping
    override fun getMyPage(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MyPageResponse> {
        return ResponseEntity.ok(
            myPageService.getMyPage(userDetails.username)
        )
    }

    @PostMapping("/logout")
    override fun logout(): ResponseEntity<Void> {
        userLogoutService.logout()
        return ResponseEntity.ok().build()
    }
}
