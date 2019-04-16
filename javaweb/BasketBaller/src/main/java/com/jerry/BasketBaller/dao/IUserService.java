package com.jerry.BasketBaller.dao;

import com.jerry.BasketBaller.common.ServerResponse;
import com.jerry.BasketBaller.dto.UserDTO;
import com.jerry.BasketBaller.model.User;

public interface IUserService {

    ServerResponse<String> register(UserDTO userDTO);

    ServerResponse<User> getUserInfo(String username);

    String login(String username,String password);

}
