package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.Gender
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthUserService(
    private val userRepository: UserRepository,
    @Value("\${oauth.default-password}") private val defaultPassword: String
) {

    @Transactional
    fun findOrCreateOAuthUser(oauthUserInfo: OAuthUserInfo, provider: String): User {
        return userRepository.findByEmail(oauthUserInfo.email)
            ?: createNewOAuthUser(oauthUserInfo, provider)
    }

    private fun createNewOAuthUser(oauthUserInfo: OAuthUserInfo, provider: String): User {
        val newUser = User(
            email = oauthUserInfo.email,
            password = defaultPassword,
            name = oauthUserInfo.name,
            profileImageUrl = oauthUserInfo.profileImage ?: "",
            gender = Gender.UNKNOWN,
            role = Role.USER,
            age = "UNKNOWN",
            coin = 0,
            provider = provider.uppercase()
        )
        return userRepository.save(newUser)
    }
}
