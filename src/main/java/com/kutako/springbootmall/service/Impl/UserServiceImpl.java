package com.kutako.springbootmall.service.Impl;

import com.kutako.springbootmall.dao.UserDao;
import com.kutako.springbootmall.dto.UserLoginRequest;
import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;
import com.kutako.springbootmall.service.UserService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest ,String email) {
        // 檢查註冊的 email
        User user = userDao.getUserByEmail(email);
        if(user != null){
            log.warn("該 email {} 已經被註冊" ,userRegisterRequest.getEmail());
            // 曾經有註冊過 ,在資料庫找到對應的 user 數據的話 ,就會噴 Exception 停止這個請求
            // 這邊回傳了 400 BAD_REQUEST 的狀態碼給前端
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用 MD5 去生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);
        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查 user 是否存在
        if(user == null) {
            log.warn("該 email {} 尚未被註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比對資料庫中所儲存的雜湊值的密碼與前端傳過來的是否一致
        if(user.getPassword().equals(hashedPassword)){
            return user;
        }else{
            log.warn("email {} 密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
