package cn.jeelearn.house.user.controller;

import cn.jeelearn.house.user.common.RestResponse;
import cn.jeelearn.house.user.model.User;
import cn.jeelearn.house.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/28
 * @Version:v1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    //-------------------查询---------------------
    @PostMapping("/list")
    public RestResponse<List<User>> list(@RequestBody User user){
        List<User> users = userService.getUserByQuery(user);
        return RestResponse.success(users);
    }

    @GetMapping("/info/{id}")
    public RestResponse<User> getUserById(@PathVariable("id") Long id){
        User user = userService.getUserById(id);
        return RestResponse.success(user);
    }

    //-------------------注册---------------------
    @PostMapping("add")
    public RestResponse<User> add(@RequestBody User user){
        userService.addAccount(user,user.getEnableUrl());
        return RestResponse.success();
    }

    /**
     * 主要激活key的验证
     */
    @GetMapping("enable")
    public RestResponse<Object> enable(String key){
        userService.enable(key);
        return RestResponse.success();
    }

    //------------------------登录--------------------------
    @PostMapping("login")
    public RestResponse<User> login(@RequestBody User user){
        User finalUser = userService.login(user.getEmail(),user.getPasswd());
        return RestResponse.success(finalUser);
    }

    @GetMapping("logout")
    public RestResponse<Object> logout(String token){
        userService.invalidate(token);
        return RestResponse.success();
    }

    //------------------------鉴权--------------------------
    @GetMapping("auth")
    public RestResponse<User> getAuthUser(String token){
        User finalUser = userService.getLoginedUserByToken(token);
        return RestResponse.success(finalUser);
    }

}

