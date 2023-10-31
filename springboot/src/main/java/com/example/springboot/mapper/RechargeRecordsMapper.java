package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.RechargeRecords;
import org.apache.ibatis.annotations.Param;

/**
 * 功能：RechargeRecords接口访问数据库
 * 日期：2023/11/13 19:18
 */
public interface RechargeRecordsMapper extends BaseMapper<RechargeRecords> {
    IPage<RechargeRecords> selectByUser_username(Page<RechargeRecords> page, @Param("user_username") String user_username);
}

