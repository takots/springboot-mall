package com.kutako.springbootmall.dao;

import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;

public interface UserDao {
    User getUserById(Integer userId);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
