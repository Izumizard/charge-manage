package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Balance;
import com.example.springboot.entity.Orders;
import com.example.springboot.mapper.BalanceMapper;
import com.example.springboot.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 功能：封装对用户余额信息进行持久化操作的方法
 * 日期：2023/11/12 18:48
 */
@Service
public class BalanceService extends ServiceImpl<BalanceMapper, Balance> {
    @Resource
    BalanceMapper balanceMapper;
    @Resource
    OrdersMapper ordersMapper;
    /**
     * 根据user_id查询数据库的用户余额
     * @param userId 用户id
     * @return
     */
    public Balance selectByUserId(Integer userId) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return getOne(queryWrapper);
    }

    /**
     * 根据user_id删除数据库的用户余额
     * @param userId 用户id
     */
    public void removeBalanceByUserId(Integer userId) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        balanceMapper.delete(queryWrapper);
    }


    /**
     * 根据user_id批量删除数据库的用户余额
     * @param userIds 多个用户id
     */
    public void removeBalancesByUserIds(List<Integer> userIds) {
        QueryWrapper<Balance> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", userIds);
        balanceMapper.delete(queryWrapper);
    }

    /**
     * 充值操作逻辑
     * @param userId 用户ID
     * @param rechargeAmount 余额
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



    /**
     * 根据订单金额扣除用户余额
     * @param user_id 用户ID
     * @param price 订单金额
     */
    public void deductBalance(Integer user_id, BigDecimal price) {
        // 1. 获取当前用户的余额信息
        Balance balance = selectByUserId(user_id);
        // 2. 检查用户余额是否足够支付订单金额
        if (balance.getAmount().compareTo(price) < 0) {
            throw new RuntimeException("用户余额不足");
        }
        // 3. 计算新的余额值
        BigDecimal newAmount = balance.getAmount().subtract(price);
        // 4. 更新余额信息
        balance.setAmount(newAmount);
        balanceMapper.updateById(balance);
        //5. 查询orders表中的userId和"待付款"状态的订单信息，并且更新订单的状态信息为"已预约"
        Orders orders = ordersMapper.selectOne(Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getUser_id, user_id)
                .eq(Orders::getStatus, "待付款"));
        if (orders != null) {
            orders.setStatus("已预约");
            ordersMapper.updateById(orders);
        }
    }



    /**
     * 根据下单金额进行退款
     */
    public void returnBalance(Integer userId) {
        Balance balance = selectByUserId(userId);
        Orders orders = ordersMapper.selectOne(new QueryWrapper<Orders>()
                .eq("user_id", userId)
                .in("status", "已预约" , "使用中"));
        BigDecimal price = orders.getPrice();
        if (orders.getStatus().equals("已预约")){
            BigDecimal newAmount = balance.getAmount().add(price);
            balance.setAmount(newAmount);
            balanceMapper.updateById(balance);
        } else if (Objects.equals(orders.getStatus(), "使用中")){
            BigDecimal amount = price.multiply(new BigDecimal("0.3333"));
            BigDecimal newAmount = balance.getAmount().add(amount);
            balance.setAmount(newAmount);
            balanceMapper.updateById(balance);
            orders.setPrice(amount);
            ordersMapper.updateById(orders);
        }

    }


}
