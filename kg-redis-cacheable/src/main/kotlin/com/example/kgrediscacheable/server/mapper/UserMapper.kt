package com.example.kgrediscacheable.server.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.example.kgrediscacheable.server.domain.User
import org.apache.ibatis.annotations.Mapper

/**
 * @author : songtc
 * @since : 2023/12/08 14:19
 */
@Mapper
interface UserMapper : BaseMapper<User> {
}