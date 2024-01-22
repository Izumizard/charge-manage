package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.config.Result;
import com.example.springboot.controller.dto.OrdersDTO;
import com.example.springboot.controller.dto.OrdersRequest;
import com.example.springboot.entity.ChargingPort;
import com.example.springboot.entity.Orders;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.OrdersMapper;
import com.example.springboot.service.BalanceService;
import com.example.springboot.service.ChargingPortService;
import com.example.springboot.service.OrdersService;
import com.example.springboot.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    UserService userService;
    @Resource
    ChargingPortService chargingPortService;
    @Resource
    BalanceService balanceService;
    @Resource
    OrdersMapper ordersMapper;

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
        if (Objects.equals(request.getStatus(), "待付款")) {
            ordersService.cancelOrderByUserId(request.getUser_id());
        } else if (Objects.equals(request.getStatus(), "已预约")) {
            balanceService.returnBalance(request.getUser_id());
            ordersService.cancelBookOrder(request.getUser_id());
        } else if (Objects.equals(request.getStatus(), "使用中")) {
            balanceService.returnBalance(request.getUser_id());
            ordersService.endOrder(request.getUser_id());
        }
        return Result.success("订单修改成功！");
    }

    /**
     * 扫码获取用户预约订单信息
     */
    @GetMapping("/selectOrder")
    public Result selectOrder(@RequestParam Integer user_id,
                              @RequestParam String port_number,
                              @RequestParam String status) {
        try {
            Orders orders = ordersService.selectOrder(user_id, port_number, status);
            return Result.success(orders);
        } catch (RuntimeException e) {
            return Result.error("300", e.getMessage());
        }
    }


    /**
     * 分页多条件模糊查询用户订单信息
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(required = false) String username) {
        IPage<Orders> page = ordersMapper.getOrdersByPage(new Page<>(pageNum, pageSize), username);
        return Result.success(page);
    }


    /**
     * 删除订单信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        ordersService.removeById(id);
        return Result.success();
    }


    /**
     * 批量删除订单信息
     */
    @DeleteMapping("/delete/batch")
    public Result batch(@RequestBody List<Integer> ids) {
        ordersService.removeBatchByIds(ids);
        return Result.success();
    }


    /**
     * 批量导出接口
     */
    @GetMapping("/export")
    public void exportData(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String ids,
                           HttpServletResponse response) throws IOException {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(ids)){   //  ["1", "2" ,"3"]  => [1, 2, 3]
            List<Integer> idsArr = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
            queryWrapper.in("id",idsArr);
        } else {
            // 全部导出或者条件导出
            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        }
        List<Orders> list = ordersService.list(queryWrapper);
        List<OrdersDTO> dtoList = list.stream()
                .map(orders -> {
                    OrdersDTO dto = new OrdersDTO();
                    BeanUtils.copyProperties(orders, dto);
                    // 根据 user_id 查询用户表获取用户名
                    User user = userService.getById(orders.getUser_id());
                    ChargingPort port = chargingPortService.getById(orders.getPort_id());
                    if (user != null) {
                        dto.setUsername(user.getUsername());
                    }
                    if (port != null) {
                        dto.setPort_number(port.getPort_number());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        writer.write(dtoList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("订单表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        outputStream.flush();
        outputStream.close();
    }

}
