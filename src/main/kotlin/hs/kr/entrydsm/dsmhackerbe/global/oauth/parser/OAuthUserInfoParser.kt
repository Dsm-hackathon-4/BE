package hs.kr.entrydsm.dsmhackerbe.global.oauth.parser

import hs.kr.entrydsm.dsmhackerbe.global.oauth.dto.OAuthUserInfo

sealed interface OAuthUserInfoParser {
    fun parse(attributes: Map<String, Any>): OAuthUserInfo
}
