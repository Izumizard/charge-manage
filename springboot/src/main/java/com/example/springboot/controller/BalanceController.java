package com.example.springboot.controller;

import com.example.springboot.common.enums.LogType;
import com.example.springboot.common.annotations.MessageLogs;
import com.example.springboot.common.config.Result;
import com.example.springboot.controller.dto.OrdersRequest;
import com.example.springboot.controller.dto.RechargeRequest;
import com.example.springboot.entity.Balance;
import com.example.springboot.service.BalanceService;
import com.example.springboot.service.RechargeRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 功能：余额充值、充值信息写入、实时查询用户余额
 * 日期：2023/11/12 23:30
 */
@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Resource
    BalanceService balanceService;
    @Resource
    RechargeRecordsService rechargeRecordsService;
    @MessageLogs(operation = "余额信息", type = LogType.RECHARGE)
    @PostMapping("/recharge")
    public Result recharge(@RequestBody RechargeRequest request) {
            balanceService.rechargeBalance(request.getUserId(), request.getRechargeAmount());
            rechargeRecordsService.createRechargeRecords(request.getUserId(), request.getRechargeAmount(), request.getUser_username());
            return Result.success("充值成功！");
    }


    @MessageLogs(operation = "余额信息", type = LogType.RESEARCH)
    @GetMapping("/selectBalance/{userId}")
    public Result selectBalance(@PathVariable Integer userId){
        Balance balance = balanceService.selectByUserId(userId);
        if (balance == null) {
            balance = new Balance();
            balance.setUserId(userId);
            balance.setAmount(BigDecimal.valueOf(0));
            balanceService.save(balance);
        }
        return Result.success(balance.getAmount());
    }

    @PostMapping("/payment")
    public Result payment(@RequestBody OrdersRequest request) {
        balanceService.deductBalance(request.getUser_id(), request.getPrice());
        return Result.success("付款成功！");
    }

}



