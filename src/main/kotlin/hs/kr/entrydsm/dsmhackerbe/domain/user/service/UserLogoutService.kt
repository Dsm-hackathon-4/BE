package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.repository.RefreshTokenRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user.UserNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserLogoutService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
) {

    fun logout() {
        val email = SecurityContextHolder.getContext().authentication.name
        val currentUser = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        refreshTokenRepository.deleteById(currentUser.email)
    }
}
