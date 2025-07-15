package hs.kr.entrydsm.dsmhackerbe.domain.user.presentation

import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.RegisterRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.UserLoginRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.TokenResponse
import hs.kr.entrydsm.dsmhackerbe.global.document.user.UserApiDocument
import hs.kr.entrydsm.dsmhackerbe.domain.user.service.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userLoginService: UserLoginService,
    private val userRegisterService: UserRegisterService,
    private val userTokenReissueService: UserTokenReissueService,
    private val userLogoutService: UserLogoutService,
    private val userWithdrawalService: UserWithdrawalService
) : UserApiDocument {

    @PostMapping("/login")
    override fun login(@Valid @RequestBody request: UserLoginRequest): TokenResponse {
        return userLoginService.login(request)
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    override fun register(@Valid @RequestBody request: RegisterRequest) {
        userRegisterService.register(request)
    }

    @PutMapping("/reissue")
    override fun reissue(@RequestHeader("X-Refresh-Token") token: String): TokenResponse {
        return userTokenReissueService.reissue(token)
    }

    @PostMapping("/logout")
    override fun logout() {
        userLogoutService.logout()
    }

    @DeleteMapping("/withdrawal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun withdrawal() {
        userWithdrawalService.withdrawal()
    }
}
