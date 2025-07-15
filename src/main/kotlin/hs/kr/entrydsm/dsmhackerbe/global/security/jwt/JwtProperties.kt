package hs.kr.entrydsm.dsmhackerbe.global.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val header: String,
    val prefix: String,
    val secret: String,
    val accessExpiration: Long,
    val refreshExpiration: Long
)
