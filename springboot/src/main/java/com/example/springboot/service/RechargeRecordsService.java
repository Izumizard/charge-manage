package com.example.springboot.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.StatusType;
import com.example.springboot.entity.RechargeRecords;
import com.example.springboot.mapper.RechargeRecordsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 功能：
 * 日期：2023/11/15 22:32
 */
@Service
public class RechargeRecordsService extends ServiceImpl<RechargeRecordsMapper, RechargeRecords> {

    //创建充值记录信息
    public void createRechargeRecords(Integer userId, BigDecimal rechargeAmount, String user_username) {
        RechargeRecords rechargeRecords = new RechargeRecords();
        rechargeRecords.setUserId(userId);
        rechargeRecords.setRecharge(rechargeAmount);
        rechargeRecords.setUser_username(user_username);
        // 生成充值单号
        String number = "CZ" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
        rechargeRecords.setNumber(number);
        rechargeRecords.setType("微信支付");
        rechargeRecords.setStatus(StatusType.SUCCESS); // 赋值枚举常量
        rechargeRecords.setRecharge_time(new Timestamp(System.currentTimeMillis()));
        save(rechargeRecords);
    }
    @Resource
    RechargeRecordsMapper rechargeRecordsMapper;
    public IPage<RechargeRecords> getRechargeRecords(Page<RechargeRecords> page, String user_username){
        return  rechargeRecordsMapper.selectByUser_username(page, user_username);
    }
}

