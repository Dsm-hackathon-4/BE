package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.UserLoginRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.TokenResponse
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user.PasswordMissMatchException
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user.UserNotFoundException
import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserLoginService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    @Transactional
    fun login(request: UserLoginRequest): TokenResponse {
        authenticateUser(request)
        return generateToken(request.email)
    }

    private fun authenticateUser(request: UserLoginRequest) {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw PasswordMissMatchException()
        }
    }

    private fun generateToken(email: String): TokenResponse {
        return jwtTokenProvider.generateToken(email, Role.USER.toString())
    }
}
