package com.example.rediscacheable;

import com.example.rediscacheable.server.domain.User;
import com.example.rediscacheable.server.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedisCacheableApplicationTests {

    @Resource
    UserServiceImpl userService;

    @Test
    void contextLoads() {
        System.out.println(userService.getUser(1L));
    }

    @Test
    void contextLoads1() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("123456");
        System.out.println(userService.updateUser(user));
    }

}
