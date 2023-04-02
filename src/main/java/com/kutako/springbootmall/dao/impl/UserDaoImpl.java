package com.kutako.springbootmall.dao.impl;

import com.kutako.springbootmall.dao.UserDao;
import com.kutako.springbootmall.dto.UserRegisterRequest;
import com.kutako.springbootmall.model.User;
import com.kutako.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer userId) {
        String SqlStr=" SELECT user_id ,email ,password ,created_date ,last_modified_date"
                     +" FROM user WHERE user_id = :userId";
        Map<String , Object> map = new HashMap<>();
        map.put("userId" ,userId);
        List<User> userList = namedParameterJdbcTemplate.query(SqlStr ,map ,new UserRowMapper());
        if(userList.size()>0){
            return userList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String SqlStr="INSERT INTO user(email ,password ,created_date ,last_modified_date)"
                     +"VALUES (:email ,:password ,:createdDate ,:lastModifiedDate)";
        Map<String , Object> map = new HashMap<>();
        map.put("email" ,userRegisterRequest.getEmail());
        map.put("password" ,userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate" ,now);
        map.put("lastModifiedDate" ,now);

        // keyHolder 接住 MySQL 資料庫自動生成的 userId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SqlStr ,new MapSqlParameterSource(map) ,keyHolder);
        int userId = keyHolder.getKey().intValue();
        return userId;
    }
}
