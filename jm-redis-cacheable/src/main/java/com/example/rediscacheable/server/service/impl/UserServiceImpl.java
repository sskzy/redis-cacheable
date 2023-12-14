package com.example.rediscacheable.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rediscacheable.redis.annotation.RedisCacheEvict;
import com.example.rediscacheable.redis.annotation.RedisCacheable;
import com.example.rediscacheable.server.domain.User;
import com.example.rediscacheable.server.mapper.UserMapper;
import com.example.rediscacheable.server.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private final String USER_KEY = "user";

    @RedisCacheable(prefix = USER_KEY, suffix = "#id")
    @Override
    public User getUser(Long id) {
        return getById(id);
    }

    @RedisCacheEvict(prefix = USER_KEY, suffix = "#user.id")
    @Override
    public Boolean updateUser(User user) {
        return updateById(user);
    }
}




