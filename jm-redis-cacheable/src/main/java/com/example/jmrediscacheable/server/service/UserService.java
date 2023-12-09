package com.example.jmrediscacheable.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jmrediscacheable.server.domain.User;

public interface UserService extends IService<User> {

    User getUser(Long id);

    Boolean updateUser(User user);
}
