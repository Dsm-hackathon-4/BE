package hs.kr.entrydsm.dsmhackerbe.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    
    @Value("\${spring.data.redis.host}")
    private lateinit var host: String
    
    @Value("\${spring.data.redis.port}")
    private var port: Int = 0
    
    @Value("\${spring.data.redis.password}")
    private lateinit var password: String
    
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()
        redisStandaloneConfiguration.hostName = host
        redisStandaloneConfiguration.port = port
        redisStandaloneConfiguration.setPassword(password)
        
        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }
    
    @Bean
    fun redisTemplate(): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        
        redisTemplate.connectionFactory = redisConnectionFactory()
        // spring-redis간 데이터 직렬화를 위해 설정
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        
        return redisTemplate
    }
}