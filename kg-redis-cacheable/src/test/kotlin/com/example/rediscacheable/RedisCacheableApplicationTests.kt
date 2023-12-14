package com.example.rediscacheable

import com.example.jgrediscacheable.serve.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.Resource

@SpringBootTest
class RedisCacheableApplicationTests {

    @Resource
    private lateinit var userService: UserService;
    @Test
    fun contextLoads() {
        println(userService.getUser(1L))
    }
}
