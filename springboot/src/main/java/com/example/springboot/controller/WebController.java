package com.example.springboot.controller;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能：提供接口返回数据
 * 日期：2023/9/12 18:19
 */
@RestController
@RequestMapping("/web")
public class WebController {

    @GetMapping ("/hello")
    public Result hello(String name){
            return Result.sucess(name);
    }
}
