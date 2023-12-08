package com.example.kgrediscacheable.redis.annotation

import org.springframework.cache.annotation.Cacheable
import org.springframework.core.annotation.AliasFor

/**
 * @author : songtc
 * @since : 2023/12/08 14:20
 */
@Cacheable(value = ["songtc"])
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheEvict(
    /** key的前缀  */
    val prefix: String,
    /** key的后缀 expression表达式  */
    @get:AliasFor(value = "condition", annotation = Cacheable::class) val suffix: String
)
