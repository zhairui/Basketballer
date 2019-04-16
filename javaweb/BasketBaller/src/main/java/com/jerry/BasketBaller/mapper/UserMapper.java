package com.jerry.BasketBaller.mapper;

import com.jerry.BasketBaller.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int checkUserName(String username);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getUserInfo(String username);

    User getUserInfo(String username,String password);
}