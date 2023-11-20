package com.example.springboot.common.annotations;

import com.example.springboot.common.enums.LogType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageLogs {
    //操作的模块
    String operation();
    //操作类型
    LogType type();
}
