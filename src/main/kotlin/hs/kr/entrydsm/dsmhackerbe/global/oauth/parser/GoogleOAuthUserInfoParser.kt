package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser

import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo
import org.springframework.stereotype.Component

@Component
class GoogleOAuthUserInfoParser : OAuthUserInfoParser {
    
    override fun parse(attributes: Map<String, Any>): OAuthUserInfo {
        return OAuthUserInfo(
            name = attributes["name"] as String,
            email = attributes["email"] as String,
            profileImage = attributes["picture"] as? String
        )
    }
}
