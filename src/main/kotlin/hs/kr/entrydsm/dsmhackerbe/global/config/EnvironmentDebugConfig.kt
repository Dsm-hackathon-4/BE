package hs.kr.entrydsm.dsmhackerbe.global.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class EnvironmentDebugConfig {
    
    private val logger = LoggerFactory.getLogger(EnvironmentDebugConfig::class.java)
    
    @Value("\${REDIS_HOST:not-set}")
    private lateinit var redisHost: String
    
    @Value("\${REDIS_PORT:0}")
    private var redisPort: Int = 0
    
    @Value("\${DB_URL:not-set}")
    private lateinit var dbUrl: String
    
    @PostConstruct
    fun logEnvironmentVariables() {
        logger.info("=== Environment Variables Debug ===")
        logger.info("REDIS_HOST: $redisHost")
        logger.info("REDIS_PORT: $redisPort")
        logger.info("DB_URL: $dbUrl")
        logger.info("===================================")
    }
}