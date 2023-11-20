package com.example.springboot.controller;

import com.example.springboot.common.annotations.AuthAccess;
import com.example.springboot.common.config.Result;
import com.example.springboot.controller.dto.OrdersRequest;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.BalanceService;
import com.example.springboot.service.OrdersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 功能：
 * 日期：2023/12/29 21:20
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Resource
    OrdersService ordersService;
    @Resource
    BalanceService balanceService;


    /**
     * 新增订单信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody OrdersRequest request) {
        ordersService.createOrders(request.getUser_id(), request.getPrice(), request.getPort_id(), request.getStation_id(), request.getStart_time(), request.getDuration());
        return Result.success("订单创建成功！");
    }

    /**
     * 查询用户的订单信息
     */
    @GetMapping("/selectByUserId/{user_id}")
    public Result selectByUserId(@PathVariable Integer user_id) {
            List<Orders> ordersList = ordersService.getInfo(user_id);
            return Result.success(ordersList);
    }


    /**
     * 用户取消订单
     */
    @PostMapping("/update")
    public Result update(@RequestBody OrdersRequest request) {
        if (Objects.equals(request.getStatus(), "待付款")){
            ordersService.cancelOrderByUserId(request.getUser_id());
        } else if (Objects.equals(request.getStatus(), "已预约")){
            balanceService.returnBalance(request.getUser_id());
            ordersService.cancelBookOrder(request.getUser_id());
        } else if (Objects.equals(request.getStatus(), "使用中")) {
            balanceService.returnBalance(request.getUser_id());
            ordersService.endOrder(request.getUser_id());
        }
        return Result.success("订单修改成功！");
    }
}
