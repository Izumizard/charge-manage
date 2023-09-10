package com.example.springboot.controller;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能：提供接口返回数据
 * 日期：2023/9/12 18:19
 */
@RestController
public class WebController {
    @RequestMapping
    public Result hello(){
            return Result.sucess("扣一");
    }
}
