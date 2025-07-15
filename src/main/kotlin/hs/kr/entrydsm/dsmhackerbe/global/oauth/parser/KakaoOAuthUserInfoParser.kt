package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser

import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.springframework.stereotype.Component

@Component
class KakaoOAuthUserInfoParser : OAuthUserInfoParser {
    
    override fun parse(attributes: Map<String, Any>): OAuthUserInfo {
        val account = attributes["kakao_account"] as Map<String, Any>
        val profile = account["profile"] as Map<String, Any>

        return OAuthUserInfo(
            name = profile["nickname"] as String,
            email = account["email"] as String,
            profileImage = profile["profile_image_url"] as? String
        )
    }
}
