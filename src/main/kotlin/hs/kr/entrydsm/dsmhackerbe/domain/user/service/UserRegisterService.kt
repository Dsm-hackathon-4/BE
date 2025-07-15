package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.request.RegisterRequest
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.user.UserAlreadyExistException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegisterService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun register(request: RegisterRequest) {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistException()
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name,
            profileImageUrl = request.profileImageUrl,
            gender = request.gender,
            role = Role.USER,
            age = request.age,
            coin = 0,
            provider = "LOCAL"
        )

        userRepository.save(user)
    }
}
