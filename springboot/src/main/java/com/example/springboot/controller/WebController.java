package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

/**
 * 功能：提供接口返回数据
 * 日期：2023/9/12 18:19
 */
@RestController

public class WebController {


    @Resource
    UserService userService;
    @GetMapping ("/")
    public Result hello(){
          return Result.success("success");
    }

    @PostMapping("/Login")
    public Result login(@RequestBody User user) {
        if (StrUtil.isBlank(user.getUsername()) || StrUtil.isBlank(user.getPassword())){
            return Result.error("用户名或密码不能为空");
        }
        user = userService.login(user);
        return  Result.success(user);
    }
}
