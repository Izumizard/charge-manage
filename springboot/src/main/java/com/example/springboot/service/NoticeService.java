package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Notice;
import com.example.springboot.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能：封装对系统公告信息进行持久化操作的方法
 * 日期：2023/10/23 18:43
 */
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {

}
