package hs.kr.entrydsm.dsmhackerbe

import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
@EnableJpaRepositories(basePackages = ["hs.kr.entrydsm.dsmhackerbe.domain.*.repository"], 
                       excludeFilters = [org.springframework.context.annotation.ComponentScan.Filter(
                           type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                           classes = [hs.kr.entrydsm.dsmhackerbe.domain.auth.repository.RefreshTokenRepository::class]
                       )])
@EnableRedisRepositories(basePackages = ["hs.kr.entrydsm.dsmhackerbe.domain.auth.repository"])
class DsmHackerBeApplication

fun main(args: Array<String>) {
    runApplication<DsmHackerBeApplication>(*args)
}
