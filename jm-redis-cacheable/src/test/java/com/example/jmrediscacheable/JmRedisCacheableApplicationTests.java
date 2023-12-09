package com.example.jmrediscacheable;

import com.example.jmrediscacheable.server.domain.User;
import com.example.jmrediscacheable.server.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class JmRedisCacheableApplicationTests {

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
