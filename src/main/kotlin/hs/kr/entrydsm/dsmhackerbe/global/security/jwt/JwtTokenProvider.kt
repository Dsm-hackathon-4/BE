package hs.kr.entrydsm.dsmhackerbe.global.security.jwt

import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.RefreshToken
import hs.kr.entrydsm.dsmhackerbe.domain.auth.entity.Role
import hs.kr.entrydsm.dsmhackerbe.domain.auth.repository.RefreshTokenRepository
import hs.kr.entrydsm.dsmhackerbe.domain.user.dto.response.TokenResponse
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.security.ExpiredTokenException
import hs.kr.entrydsm.dsmhackerbe.global.exception.domain.security.InvalidJwtException
import hs.kr.entrydsm.dsmhackerbe.global.security.auth.CustomAdminDetailsService
import hs.kr.entrydsm.dsmhackerbe.global.security.auth.CustomUserDetailsService
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val customUserDetailsService: CustomUserDetailsService,
    private val customAdminDetailsService: CustomAdminDetailsService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
    }

    private val secretKey = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(id: String, role: String): TokenResponse {
        return try {
            logger.info("토큰 생성 시작 - User ID: $id, Role: $role")
            
            val accessToken = generateAccessToken(id, role, ACCESS_TOKEN, jwtProperties.accessExpiration)
            val refreshToken = generateRefreshToken(role, REFRESH_TOKEN, jwtProperties.refreshExpiration)
            
            logger.info("JWT 토큰 생성 완료 - User ID: $id")
            logger.info("Redis에 RefreshToken 저장 시도 - User ID: $id")
            
            refreshTokenRepository.save(
                RefreshToken(id, refreshToken, jwtProperties.refreshExpiration)
            )
            
            logger.info("Redis에 RefreshToken 저장 완료 - User ID: $id")
            
            TokenResponse(accessToken, refreshToken)
        } catch (e: Exception) {
            logger.error("토큰 생성 실패 - User ID: $id", e)
            throw e
        }
    }

    private fun generateAccessToken(id: String, role: String, type: String, exp: Long): String {
        val now = Date()
        
        return Jwts.builder()
            .setSubject(id)
            .claim("type", type)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + exp * 1000))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun generateRefreshToken(role: String, type: String, exp: Long): String {
        val now = Date()
        
        return Jwts.builder()
            .claim("type", type)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + exp * 1000))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun reissue(refreshToken: String): TokenResponse {
        if (!isRefreshToken(refreshToken)) {
            throw InvalidJwtException()
        }
        
        val token = refreshTokenRepository.findByToken(refreshToken)
            ?: throw InvalidJwtException()

        val id = token.id
        val role = getRole(token.token)

        val tokenResponse = generateToken(id, role)
        
        token.update(tokenResponse.refreshToken, jwtProperties.refreshExpiration)
        refreshTokenRepository.save(token)

        return tokenResponse
    }

    private fun getRole(token: String): String {
        return getJws(token).body["role"].toString()
    }

    private fun isRefreshToken(token: String?): Boolean {
        if (token.isNullOrEmpty()) return false

        return try {
            val claims = getJws(token).body
            val type = claims["type"] as? String
            REFRESH_TOKEN == type
        } catch (e: Exception) {
            false
        }
    }

    private fun getJws(token: String): Jws<Claims> {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        } catch (e: Exception) {
            throw InvalidJwtException()
        }
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(jwtProperties.header)

        return if (StringUtils.hasText(bearerToken) && 
                   bearerToken.startsWith(jwtProperties.prefix) &&
                   bearerToken.length > jwtProperties.prefix.length + 1) {
            bearerToken.substring(jwtProperties.prefix.length + 1)
        } else null
    }

    fun getAuthentication(token: String): Authentication {
        val body = getJws(token).body
        val userDetails = getDetails(body)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun validateToken(token: String): Boolean {
        return try {
            getJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getDetails(body: Claims): UserDetails {
        val role = body["role"].toString()

        // whispy be와 같이 role에 따라 다른 UserDetailsService 사용
        return if (Role.ADMIN.toString() == role) {
            customAdminDetailsService.loadUserByUsername(body.subject)
        } else {
            customUserDetailsService.loadUserByUsername(body.subject)
        }
    }
}
