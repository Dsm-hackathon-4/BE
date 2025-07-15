package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.repository.RefreshTokenRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user.UserNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserWithdrawalService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    @Transactional
    fun withdrawal() {
        val email = SecurityContextHolder.getContext().authentication.name
        val currentUser = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        
        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteById(currentUser.email)
        
        // 유저 삭제
        currentUser.id?.let { userRepository.deleteById(it) }
    }
}
