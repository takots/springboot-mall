package com.kutako.springbootmall.service;

import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;

public interface UserService {
    User getUserById(Integer userId);
    Integer register(UserRegisterRequest userRegisterRequest ,String email);
}
