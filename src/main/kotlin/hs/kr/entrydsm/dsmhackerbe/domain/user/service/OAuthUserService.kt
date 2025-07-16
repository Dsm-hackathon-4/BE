package hs.kr.entrydsm.dsmhackerbe.domain.user.service

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.Gender
import hs.kr.entrydsm.dsmhackerbe.domain.user.entity.User
import hs.kr.entrydsm.dsmhackerbe.domain.user.repository.UserRepository
import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuthUserService(
    private val userRepository: UserRepository,
    @Value("\${oauth.default-password}") private val defaultPassword: String
) {
    
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun findOrCreateOAuthUser(oauthUserInfo: OAuthUserInfo, provider: String): User {
        logger.info("OAuth 사용자 찾기/생성 시작 - Email: ${oauthUserInfo.email}, Provider: $provider")
        
        return userRepository.findByEmail(oauthUserInfo.email)?.also {
            logger.info("기존 사용자 발견 - UserId: ${it.id}")
        } ?: createNewOAuthUser(oauthUserInfo, provider).also {
            logger.info("새 사용자 생성 완료 - UserId: ${it.id}")
        }
    }

    private fun createNewOAuthUser(oauthUserInfo: OAuthUserInfo, provider: String): User {
        logger.info("새 OAuth 사용자 생성 중 - Email: ${oauthUserInfo.email}")
        
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
