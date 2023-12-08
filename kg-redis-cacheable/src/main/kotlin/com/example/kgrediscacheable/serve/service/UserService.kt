package com.example.jgrediscacheable.serve.service

import com.baomidou.mybatisplus.extension.service.IService
import com.example.kgrediscacheable.serve.domain.User

/**
 * @author : songtc
 * @since : 2023/12/08 14:16
 */
interface UserService : IService<User> {
    fun getUser(id: Long): User

    fun updateUser(user: User): Boolean
}