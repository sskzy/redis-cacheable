package com.example.kgrediscacheable

import com.example.jgrediscacheable.serve.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import javax.annotation.Resource

@SpringBootTest
class KgRedisCacheableApplicationTests {

    @Resource
    private lateinit var userService: UserService;
    @Test
    fun contextLoads() {
        println(userService.getUser(1L))
    }
}
