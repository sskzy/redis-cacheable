package com.example.jgrediscacheable.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * @author : songtc
 * @since : 2023/12/08 14:20
 */
@Configuration
class RedisConfig {
    @Bean
    fun redisStringTemplate(redisTemplate: RedisTemplate<Any?, Any?>): RedisTemplate<Any?, Any?> {
        val stringRedisSerializer = StringRedisSerializer()
        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.valueSerializer = stringRedisSerializer
        return redisTemplate
    }
}