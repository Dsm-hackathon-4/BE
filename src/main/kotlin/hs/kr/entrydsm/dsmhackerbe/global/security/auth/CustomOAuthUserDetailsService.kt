package hs.kr.entrydsm.dsmhackerbe.global.security.auth

import hs.kr.entrydsm.dsmhackerbe.domain.user.service.OAuthUserService
import hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.factory.OAuthUserInfoParserFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuthUserDetailsService(
    private val oauthUserService: OAuthUserService,
    private val oauthUserInfoParserFactory: OAuthUserInfoParserFactory
) : DefaultOAuth2UserService() {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val provider = userRequest.clientRegistration.registrationId

        val parser = oauthUserInfoParserFactory.getParser(provider)
        val oauthUserInfo = parser.parse(oAuth2User.attributes)
        val user = oauthUserService.findOrCreateOAuthUser(oauthUserInfo, provider)

        return AuthDetails(user.email, user.role.name, oAuth2User.attributes)
    }
}
