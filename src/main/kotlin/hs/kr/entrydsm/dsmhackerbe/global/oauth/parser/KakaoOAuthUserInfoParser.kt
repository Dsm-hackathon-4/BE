package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser

import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class KakaoOAuthUserInfoParser : OAuthUserInfoParser {
    
    private val logger = LoggerFactory.getLogger(this::class.java)
    
    override fun parse(attributes: Map<String, Any>): OAuthUserInfo {
        return try {
            logger.debug("Kakao OAuth 속성 파싱 시작: $attributes")
            
            val account = attributes["kakao_account"] as? Map<String, Any>
                ?: throw IllegalArgumentException("Kakao OAuth에서 kakao_account를 찾을 수 없습니다")
            val profile = account["profile"] as? Map<String, Any>
                ?: throw IllegalArgumentException("Kakao OAuth에서 profile을 찾을 수 없습니다")

            val name = profile["nickname"] as? String
                ?: throw IllegalArgumentException("Kakao OAuth에서 nickname을 찾을 수 없습니다")
            val email = account["email"] as? String
                ?: throw IllegalArgumentException("Kakao OAuth에서 email을 찾을 수 없습니다")
            val profileImage = profile["profile_image_url"] as? String
            
            logger.info("Kakao OAuth 파싱 완료 - Email: $email")

            OAuthUserInfo(
                name = name,
                email = email,
                profileImage = profileImage
            )
        } catch (e: Exception) {
            logger.error("Kakao OAuth 속성 파싱 실패: $attributes", e)
            throw e
        }
    }
}
