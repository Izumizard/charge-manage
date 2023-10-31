package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Logs;
import com.example.springboot.mapper.LogsMapper;
import org.springframework.stereotype.Service;

/**
 * 功能：封装对系统日志信息进行持久化操作的方法
 * 日期：2023/10/23 18:43
 */
@Service
public class LogsService extends ServiceImpl<LogsMapper, Logs> {


}
