package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser

import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GoogleOAuthUserInfoParser : OAuthUserInfoParser {
    
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    override fun parse(attributes: Map<String, Any>): OAuthUserInfo {
        return try {
            logger.debug("Google OAuth 속성 파싱 시작: $attributes")
            
            val name = attributes["name"] as? String 
                ?: throw IllegalArgumentException("Google OAuth에서 name을 찾을 수 없습니다")
            val email = attributes["email"] as? String 
                ?: throw IllegalArgumentException("Google OAuth에서 email을 찾을 수 없습니다")
            val profileImage = attributes["picture"] as? String
            
            logger.info("Google OAuth 파싱 완료 - Email: $email")
            
            OAuthUserInfo(
                name = name,
                email = email,
                profileImage = profileImage
            )
        } catch (e: Exception) {
            logger.error("Google OAuth 속성 파싱 실패: $attributes", e)
            throw e
        }
    }
}
