package com.example.jmrediscacheable.service.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jmrediscacheable.service.domain.User;

public interface UserService extends IService<User> {

    User getUser(Long id);

    Boolean updateUser(User user);
}
