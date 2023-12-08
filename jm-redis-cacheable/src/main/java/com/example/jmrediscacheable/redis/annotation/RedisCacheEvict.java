package com.example.jmrediscacheable.redis.annotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : songtc
 * @since : 2023/11/28 14:15
 */
@Cacheable(value = "songtc")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheEvict {
    /** key的前缀 */
    String prefix();

    /** key的后缀 expression表达式 */
    @AliasFor(value = "condition", annotation = Cacheable.class)
    String suffix();
}
