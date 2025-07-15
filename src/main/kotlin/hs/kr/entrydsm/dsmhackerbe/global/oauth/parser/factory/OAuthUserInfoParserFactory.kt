package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.factory

import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.oauth.UnsupportedProviderException
import hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.GoogleOAuthUserInfoParser
import hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.KakaoOAuthUserInfoParser
import hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.OAuthUserInfoParser
import org.springframework.stereotype.Component

@Component
class OAuthUserInfoParserFactory(
    private val googleParser: GoogleOAuthUserInfoParser,
    private val kakaoParser: KakaoOAuthUserInfoParser
) {

    fun getParser(provider: String): OAuthUserInfoParser {
        return when (provider.lowercase()) {
            "google" -> googleParser
            "kakao" -> kakaoParser
            else -> throw UnsupportedProviderException()
        }
    }
}
