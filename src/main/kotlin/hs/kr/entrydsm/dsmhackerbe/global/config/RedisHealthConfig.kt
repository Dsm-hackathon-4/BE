package hs.kr.entrydsm.dsmhackerbe.global.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisHealthConfig {
    
    private val logger = LoggerFactory.getLogger(RedisHealthConfig::class.java)
    
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>
    
    @PostConstruct
    fun checkRedisConnection() {
        try {
            redisTemplate.opsForValue().set("health-check", "ok")
            val result = redisTemplate.opsForValue().get("health-check")
            logger.info("Redis 연결 상태: SUCCESS - $result")
            redisTemplate.delete("health-check")
        } catch (e: Exception) {
            logger.error("Redis 연결 실패: ${e.message}", e)
        }
    }
}