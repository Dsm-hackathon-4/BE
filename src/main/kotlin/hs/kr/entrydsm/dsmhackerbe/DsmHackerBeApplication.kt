package hs.kr.entrydsm.dsmhackerbe

import hs.kr.entrydsm.dsmhackerbe.global.security.jwt.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class DsmHackerBeApplication

fun main(args: Array<String>) {
    runApplication<DsmHackerBeApplication>(*args)
}
