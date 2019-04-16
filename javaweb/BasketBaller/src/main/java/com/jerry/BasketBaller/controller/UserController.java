package com.jerry.BasketBaller.controller;

import com.jerry.BasketBaller.common.ServerResponse;
import com.jerry.BasketBaller.dao.IUserService;
import com.jerry.BasketBaller.dao.RedisService;
import com.jerry.BasketBaller.dto.UserDTO;
import com.jerry.BasketBaller.model.User;
import com.jerry.BasketBaller.swagger.LoginParam;
import com.jerry.BasketBaller.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "UserController",description = "用户相关")
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestBody UserDTO userDTO){
        return iUserService.register(userDTO);
    }

    @ApiOperation(value = "用户登录返回token")
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Map<String,String>> login(@RequestBody LoginParam loginParam){
        if(loginParam == null || StringUtils.isBlank(loginParam.getUsername()) || StringUtils.isBlank(loginParam.getPassword()))
            return ServerResponse.createByErrorMessage("参数错误");
        String token = iUserService.login(loginParam.getUsername(),loginParam.getPassword());
        if(token == null){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHeader",tokenHeader);
        return ServerResponse.createBySuccess(tokenMap);
    }

    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public ServerResponse<User> getUserInfo(@RequestParam String username, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = null;
        if(redisService.get(token)!=null){
            user = JsonUtil.jsonToObject(redisService.get(token),User.class);

        }
        if(user ==null){
            user = iUserService.getUserInfo(username).getData();
            redisService.set(token, JsonUtil.objectToJson(user));
        }
        return ServerResponse.createBySuccess(user);
    }



}


