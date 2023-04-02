package com.kutako.springbootmall.service.Impl;

import com.kutako.springbootmall.dao.UserDao;
import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;
import com.kutako.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
