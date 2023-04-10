package com.kutako.springbootmall.dao;

import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;

public interface UserDao {
    User getUserById(Integer userId);

    User getUserByEmail(String email);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
