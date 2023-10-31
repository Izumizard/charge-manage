package com.example.springboot.controller;

import com.example.springboot.common.AuthAccess;
import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能：提供接口返回数据
 * 日期：2023/9/12 18:19
 */
@RestController

public class WebController {


    @AuthAccess
    @GetMapping ("/")
    public Result hello(){
          return Result.success("success");
    }


}
