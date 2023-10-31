package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Balance;
import com.example.springboot.mapper.BalanceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 功能：封装对用户余额信息进行持久化操作的方法
 * 日期：2023/11/12 18:48
 */
@Service
public class BalanceService extends ServiceImpl<BalanceMapper, Balance> {
    @Resource
    BalanceMapper balanceMapper;

    /**
     * 根据user_id查询数据库的用户余额
     * @param userId
     * @return
     */
    public Balance selectByUserId(Integer userId) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return getOne(queryWrapper);
    }

    /**
     * 根据user_id删除数据库的用户余额
     * @param userId
     */
    public void removeBalanceByUserId(Integer userId) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        balanceMapper.delete(queryWrapper);
    }


    /**
     * 根据user_id批量删除数据库的用户余额
     * @param userIds
     */
    public void removeBalancesByUserIds(List<Integer> userIds) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        balanceMapper.delete(queryWrapper);
    }

    /**
     * 充值操作逻辑
     * @param userId
     * @param rechargeAmount
     */
    public void rechargeBalance(Integer userId, BigDecimal rechargeAmount) {
        // 1. 获取当前用户的余额信息
        Balance balance = balanceMapper.selectOne(new QueryWrapper<Balance>().eq("user_id", userId));
        // 2. 计算新的余额值
        BigDecimal newAmount = balance.getAmount().add(rechargeAmount);
        // 3. 更新余额信息
        balance.setAmount(newAmount);
        balanceMapper.updateById(balance);
    }

}
