package hs.kr.entrydsm.dsmhackerbe.global.security.auth

import hs.kr.entrydsm.dsmhackerbe.domain.user.service.OAuthUserService
import hs.kr.entrydsm.dsmhackerbe.global.oauth.parser.factory.OAuthUserInfoParserFactory
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        return try {
            logger.info("OAuth 로그인 시도 - Provider: ${userRequest.clientRegistration.registrationId}")
            
            val oAuth2User = super.loadUser(userRequest)
            val provider = userRequest.clientRegistration.registrationId
            
            logger.info("OAuth 사용자 정보 조회 성공 - Provider: $provider")
            logger.debug("OAuth 사용자 속성: ${oAuth2User.attributes}")

            val parser = oauthUserInfoParserFactory.getParser(provider)
            val oauthUserInfo = parser.parse(oAuth2User.attributes)
            
            logger.info("OAuth 사용자 정보 파싱 완료 - Email: ${oauthUserInfo.email}")
            
            val user = oauthUserService.findOrCreateOAuthUser(oauthUserInfo, provider)
            
            logger.info("OAuth 사용자 처리 완료 - UserId: ${user.id}")

            AuthDetails(user.email, user.role.name, oAuth2User.attributes)
        } catch (e: Exception) {
            logger.error("OAuth 인증 실패", e)
            throw OAuth2AuthenticationException("OAuth 인증 처리 중 오류 발생: ${e.message}")
        }
    }
}
