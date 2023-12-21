package com.example.rediscacheable.redis.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.example.rediscacheable.redis.annotation.RedisCacheEvict;
import com.example.rediscacheable.redis.annotation.RedisCacheable;
import com.example.rediscacheable.redis.exception.SpELParameterMissException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 环绕切入处理器
 *
 * @author : songtc
 * @since : 2023/11/28 22:48
 */
@Aspect
@Component
public final class RedisCacheAspect {

    private RedisCacheAspect() {
    }

    /**
     * 方法对象参数名称和参数数据的存储容器
     */
    private EvaluationContext context;

    /**
     * springframework 默认的 SpEL 解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 自定义 redisTemplate 模板注入
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取拼接转后缓存 key
     *
     * @param prefix 前缀
     * @param suffix SpEL后缀
     * @return 获取拼接转后缓存 key
     */
    public String convertKey(String prefix, String suffix) {
        String value = parser.parseExpression(suffix).getValue(context, String.class);
        if (ObjectUtils.isEmpty(value) || value.contains("null")) {
            throw new SpELParameterMissException("SpEL表达式:[" + suffix + "]与参数不匹配");
        }
        return prefix + ":" + value;
    }

    /**
     * EvaluationContext 装配数据
     *
     * @param argNames 参数名称
     * @param params   参数数据
     */
    public void assembling(String[] argNames, Object[] params) {
        context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) {
            context.setVariable(argNames[i], params[i]);
        }
    }

    /**
     *
     * @param prefix
     * @param suffix
     */
    private void exceptionInspection(String prefix, String suffix) {
        Assert.notNull(redisTemplate, "redisTemple cannot null");
        Assert.hasLength(prefix, "redis key prefix must not be empty");
        Assert.hasLength(suffix, "redis key suffix must not be empty");
    }

    /**
     * Annotation get cache aspect
     */
    @Around("@annotation(redisCacheable)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheable redisCacheable) throws Throwable {
        exceptionInspection(redisCacheable.prefix(), redisCacheable.suffix());

        assembling(((MethodSignature) joinPoint.getSignature()).getParameterNames(), joinPoint.getArgs());
        // data for cache
        String cacheStr = redisTemplate.opsForValue().get(convertKey(redisCacheable.prefix(), redisCacheable.suffix()));
        if (StringUtils.hasLength(cacheStr)) {
            return JSONObject.parseObject(cacheStr, ((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType());
        }
        // data for datasource
        Object result = joinPoint.proceed();
        // save data
        if (!ObjectUtils.isEmpty(result)) {
            redisTemplate.opsForValue().set(convertKey(redisCacheable.prefix(), redisCacheable.suffix()),
                    JSONObject.toJSONString(result),
                    redisCacheable.timeout(), redisCacheable.unit());
        }
        return result;
    }

    /**
     * Annotation remove cache aspect
     */
    @Around("@annotation(redisCacheEvict)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheEvict redisCacheEvict) throws Throwable {
        exceptionInspection(redisCacheEvict.prefix(), redisCacheEvict.suffix());

        assembling(((MethodSignature) joinPoint.getSignature()).getParameterNames(), joinPoint.getArgs());
        // remove cache
        redisTemplate.delete(convertKey(redisCacheEvict.prefix(), redisCacheEvict.suffix()));
        return joinPoint.proceed();
    }

    /**
     * redis相关操作 多线程扩展
     */
    public class RedisOperation {

        Callable<String> getCache(String prefix, String suffix) {
            return new Callable<String>() {
                @Override
                public String call() {
                    return redisTemplate.opsForValue().get(convertKey(prefix, suffix));
                }
            };
        }

        Runnable putObject(String prefix, String suffix, String obj, long timeout, TimeUnit timeUnit) {
            return new Runnable() {
                @Override
                public void run() {
                    redisTemplate.opsForValue().set(convertKey(prefix, suffix), obj, timeout, timeUnit);
                }
            };
        }

        Runnable removeObject(String prefix, String suffix) {
            return new Runnable() {
                @Override
                public void run() {
                    redisTemplate.delete(convertKey(prefix, suffix));
                }
            };
        }
    }

}
