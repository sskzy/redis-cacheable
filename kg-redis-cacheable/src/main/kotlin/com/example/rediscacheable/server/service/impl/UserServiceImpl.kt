package com.example.rediscacheable.server.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.example.jgrediscacheable.serve.service.UserService
import com.example.rediscacheable.redis.annotation.RedisCacheEvict
import com.example.rediscacheable.redis.annotation.RedisCacheable
import com.example.rediscacheable.server.domain.User
import com.example.rediscacheable.server.mapper.UserMapper
import org.springframework.stereotype.Service

/**
 * @author : songtc
 * @since : 2023/12/08 14:18
 */

@Service
class UserServiceImpl : ServiceImpl<UserMapper, User>(), UserService {

    private val USER_KEY = "user"
    @RedisCacheable(prefix = "user", suffix = "#id")
    override fun getUser(id: Long): User {
        return getById(id)
    }

    @RedisCacheEvict(prefix = "user", suffix = "#user.id")
    override fun updateUser(user: User): Boolean {
        return updateById(user)
    }
}
