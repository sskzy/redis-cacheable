package com.example.rediscacheable.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rediscacheable.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}




