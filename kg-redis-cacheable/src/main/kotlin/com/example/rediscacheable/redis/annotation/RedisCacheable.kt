package com.example.rediscacheable.redis.annotation

import org.springframework.cache.annotation.Cacheable
import org.springframework.core.annotation.AliasFor
import java.util.concurrent.TimeUnit

/**
 * @author : songtc
 * @since : 2023/12/08 14:20
 */
@Cacheable(value = ["songtc"])
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisCacheable(
    /** key的前缀  */
    val prefix: String,
    /** key的后缀 expression表达式  */
    @get:AliasFor(value = "condition", annotation = Cacheable::class) val suffix: String,
    /** timeout 时长  */
    val timeout: Long = 10,
    /** unit 时间单位  */
    val unit: TimeUnit = TimeUnit.MINUTES
)
