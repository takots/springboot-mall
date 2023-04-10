package com.kutako.springbootmall.controller;

import com.kutako.springbootmall.dto.UserLoginRequest;
import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;
import com.kutako.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        // register 方法選擇 POST 的兩個理由
        // 1. RESTful 中 ,創建資源對應到 POST 方法
        // 2. 資安考量 ,需要使用 request body 傳遞參數

        // 預期 userService 會有一個 register 的方法去創建一個使用者帳號 ,回傳 userId
        Integer userId = userService.register(userRegisterRequest ,userRegisterRequest.getEmail());
        // 藉由回傳的 userId 我們可以去查詢用這個 user 的資訊
        User user = userService.getUserById(userId);
        // 回應狀態碼 201 CREATED ,並將 user 創建出來的資訊放到 body 裡面
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
