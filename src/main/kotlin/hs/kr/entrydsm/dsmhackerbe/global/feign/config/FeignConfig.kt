package hs.kr.entrydsm.dsmhackerbe.global.feign.config

import feign.Retryer
import feign.codec.ErrorDecoder
import hs.kr.entrydsm.dsmhackerbe.global.feign.error.CustomErrorDecoder
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["hs.kr.entrydsm.dsmhackerbe"])
@Configuration
class FeignConfig {

    @Bean
    fun errorDecoder(): ErrorDecoder = CustomErrorDecoder()

    @Bean
    fun retryer(): Retryer = Custom5xxRetryer()
}
