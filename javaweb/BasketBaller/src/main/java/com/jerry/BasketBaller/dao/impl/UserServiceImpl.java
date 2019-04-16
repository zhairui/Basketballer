package com.jerry.BasketBaller.dao.impl;

import com.jerry.BasketBaller.common.ResponseCode;
import com.jerry.BasketBaller.common.ServerResponse;
import com.jerry.BasketBaller.dao.IUserService;
import com.jerry.BasketBaller.dto.UserDTO;
import com.jerry.BasketBaller.mapper.UserMapper;
import com.jerry.BasketBaller.model.User;
import com.jerry.BasketBaller.utils.JwtTokenUtil;
import com.jerry.BasketBaller.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<String> register(UserDTO userDTO) {
        if(userDTO == null || StringUtils.isBlank(userDTO.getUsername()) || StringUtils.isBlank(userDTO.getPassword())){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"参数错误");
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);

        int user_count = userMapper.checkUserName(user.getUsername());
        if(user_count > 0){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        int result = userMapper.insert(user);

        if(result == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<User> getUserInfo(String username) {
        if(StringUtils.isBlank(username)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"参数错误");
        }

        User user = userMapper.getUserInfo(username);
        ServerResponse serverResponse = ServerResponse.createBySuccess(user);
        if(user != null){
            return serverResponse;
        }
        return ServerResponse.createByErrorMessage("用户不存在");
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            token = jwtTokenUtil.generateToken(userDetails);
        }catch (Exception e){
            log.warn("登录异常:{}",e.getMessage());
        }
        return token;
//        int user_count = userMapper.checkUserName(username);
//        if(user_count == 0) {
//            return ServerResponse.createByErrorMessage("用户不存在");
//        }
//        String md5Password = MD5Util.MD5EncodeUtf8(password);
//        User user = userMapper.getUserInfo(username,md5Password);
//        if(user == null){
//            return ServerResponse.createByErrorMessage("用户名或密码错误");
//        }
//        return ServerResponse.createBySuccess("登录成功",user);
    }



}
