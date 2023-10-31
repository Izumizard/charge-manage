package com.example.springboot.service.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.example.springboot.common.MessageLogs;
import com.example.springboot.entity.Logs;
import com.example.springboot.entity.User;
import com.example.springboot.service.LogsService;
import com.example.springboot.utils.IpUtils;
import com.example.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能：切面
 * 日期：2023/11/9 18:59
 */
@Component
@Aspect
@Slf4j
public class LogsAspect {


    @Resource
    LogsService logsService;

    @AfterReturning(pointcut = "@annotation(messageLogs)", returning = "jsonResult")
    public void recordLog(JoinPoint joinPoint, MessageLogs messageLogs, Object jsonResult) {
        User loginUser = TokenUtils.getCurrentUser();
        if (loginUser == null) {  // 用户未登录的时候 loginUser为null 为null的话就要从参数里面获取操作人信息
            //登录、注册
            Object[] args = joinPoint.getArgs();
            if (ArrayUtil.isNotEmpty(args)) {
                if (args[0] instanceof User) {
                    loginUser = (User) args[0];
                }
            }
        }
        if (loginUser == null) {
            log.error("记录操作日志出错，未获取当前操作用户信息");
            return;
        }
        //获取HttpServletRequest对象
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取IP信息
        String ipAddr = IpUtils.getIpAddr(request);
        //组装日志的实体对象
        Logs logs = Logs.builder()
                .user(loginUser.getUsername())
                .operation(messageLogs.operation())
                .type(messageLogs.type().getValue())
                .ip(ipAddr)
                .time(DateUtil.now())
                .build();

        //插入数据到数据库
        ThreadUtil.execAsync(() -> {
            logsService.save(logs);
        });
    }
}



