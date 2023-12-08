package com.example.jmrediscacheable.redis.annotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : songtc
 * @since : 2023/11/28 14:15
 */
@Cacheable(value = "songtc")
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheable {
    /** key的前缀 */
    String prefix();

    /** key的后缀 expression表达式 */
    @AliasFor(value = "condition", annotation = Cacheable.class)
    String suffix();

    /** timeout 时长 */
    long timeout() default 10;

    /** unit 时间单位 */
    TimeUnit unit() default TimeUnit.MINUTES;
}
