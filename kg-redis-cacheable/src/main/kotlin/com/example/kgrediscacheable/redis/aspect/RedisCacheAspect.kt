package com.example.kgrediscacheable.redis.aspect

import com.example.kgrediscacheable.redis.annotation.RedisCacheEvict
import com.example.kgrediscacheable.redis.annotation.RedisCacheable
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

/**
 * 环绕切入处理器
 *
 * @author : songtc
 * @since : 2023/11/28 22:48
 */
@Aspect
@Component
class RedisCacheAspect private constructor() {

    /** 方法对象参数名称和参数数据的存储容器  */
    private var context: EvaluationContext? = null

    /** springframework 默认的 SpEL 解析器  */
    private val parser: ExpressionParser = SpelExpressionParser()

    /** 自定义 redisTemplate 模板注入  */
    @Resource
    private lateinit var redisTemplate: RedisTemplate<String, String>;

    /**
     * 获取拼接转后缓存 key
     *
     * @param prefix     前缀
     * @param SpELSuffix SpEL后缀
     * @return 获取拼接转后缓存 key
     */
    fun convertKey(prefix: String, SpELSuffix: String): String {
        return prefix + ":" + parser.parseExpression(SpELSuffix).getValue(context, String::class.java)
    }

    /**
     * EvaluationContext 装配数据
     *
     * @param argNames 参数名称
     * @param params   参数数据
     */
    fun assembling(argNames: Array<String>, params: Array<Any>) {
        context = StandardEvaluationContext()
        for (i in params.indices) {
            (context as StandardEvaluationContext).setVariable(argNames[i], params[i])
        }
    }

    /**
     * Annotation get cache aspect
     */
    @Around("@annotation(redisCacheable)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, redisCacheable: RedisCacheable): Any {
        Assert.hasLength(redisCacheable.prefix, "the redis key prefix must not be empty")
        Assert.hasLength(redisCacheable.suffix, "the redis key suffix must not be empty")
        assembling((joinPoint.signature as org.aspectj.lang.reflect.MethodSignature).parameterNames, joinPoint.args)

        // data for cache
        val cacheStr = redisTemplate.opsForValue().get(convertKey(redisCacheable.prefix, redisCacheable.suffix))
        if (StringUtils.hasLength(cacheStr)) {
            return com.alibaba.fastjson2.JSONObject.parseObject(
                cacheStr, (joinPoint.signature as org.aspectj.lang.reflect.MethodSignature).method.returnType
            )
        }
        // data for datasource
        val result: Any = joinPoint.proceed()
        // save data
        if (!ObjectUtils.isEmpty(result)) {
            redisTemplate.opsForValue().set(
                convertKey(redisCacheable.prefix, redisCacheable.suffix),
                com.alibaba.fastjson2.JSONObject.toJSONString(result),
                redisCacheable.timeout, redisCacheable.unit
            )
        }
        return result
    }

    /**
     * Annotation remove cache aspect
     */
    @Around("@annotation(redisCacheEvict)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, redisCacheEvict: RedisCacheEvict): Any {
        Assert.hasLength(redisCacheEvict.prefix, "the redis key prefix must not be empty")
        Assert.hasLength(redisCacheEvict.suffix, "the redis key suffix must not be empty")
        assembling((joinPoint.signature as org.aspectj.lang.reflect.MethodSignature).parameterNames, joinPoint.args)

        // remove cache
        redisTemplate.delete(convertKey(redisCacheEvict.prefix, redisCacheEvict.suffix))
        return joinPoint.proceed()
    }

    /**
     * redis相关操作 用于后期扩展
     */
    inner class RedisOperation {
        fun getCache(prefix: String, suffix: String): Callable<String> {
            return Callable { redisTemplate.opsForValue().get(convertKey(prefix, suffix)) }
        }

        fun putObject(prefix: String, suffix: String, obj: String, timeout: Long, timeUnit: TimeUnit): Runnable {
            return Runnable { redisTemplate.opsForValue().set(convertKey(prefix, suffix), obj, timeout, timeUnit) }
        }

        fun removeObject(prefix: String, suffix: String): Runnable {
            return Runnable { redisTemplate.delete(convertKey(prefix, suffix)) }
        }
    }
}
