package com.example.jmrediscacheable.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.jmrediscacheable.serve.domain.User;

public interface UserService extends IService<User> {

    User getUser(Long id);

    Boolean updateUser(User user);
}
