package com.example.springboot.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.constants.MqConstants;
import com.example.springboot.common.mq.DelayMessageProcessor;
import com.example.springboot.controller.dto.MultiDelayMessage;
import com.example.springboot.entity.Balance;
import com.example.springboot.entity.Orders;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.BalanceMapper;
import com.example.springboot.mapper.OrdersMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 功能：封装对系统公告信息进行持久化操作的方法
 * 日期：2023/10/23 18:43
 */
@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> {


    @Resource
    OrdersMapper ordersMapper;
    @Resource
    ChargingPortService chargingPortService;
    @Resource
    RabbitTemplate rabbitTemplate;

    // 创建订单信息
    public void createOrders(Integer user_id, BigDecimal price, Integer port_id, Integer station_id, Timestamp start_time, Integer duration) {
        String status = chargingPortService.checkStatus(port_id);
        if (!"空闲中".equals(status)){
            throw new ServiceException("300", "充电桩已被预约！");
        } else {
            Orders orders = new Orders();
            orders.setUser_id(user_id);
            orders.setPrice(price);
            orders.setPort_id(port_id);
            orders.setStation_id(station_id);
            orders.setDuration(duration);
            //生成订单号
            String number = "CDZ" + System.currentTimeMillis() + RandomUtil.randomNumbers(4);
            orders.setOrder_no(number);
            orders.setStatus("待付款");
            //订单创建时间
            orders.setCreate_time(new Timestamp(System.currentTimeMillis()));
            //订单预约开始时间
            orders.setStart_time(start_time);
            //订单预约结束时间
            Date startDate = new Date(start_time.getTime());
            // 计算结束时间
            DateTime endDate = DateUtil.offsetHour(startDate, duration);
            // 将结束时间转换为 Timestamp 对象
            Timestamp end_time = new Timestamp(endDate.getTime());
            String stationName = ordersMapper.getStationName(port_id);
            if (stationName != null) {
                orders.setStation_name(stationName);
            }
            // 判断结束时间是否跨日
            if (end_time.getDay() > start_time.getDay()) {
                // 结束时间跨日
                long timeMillis = end_time.getTime() - 24 * 3600 * 1000; // 减去一天的毫秒数
                end_time = new Timestamp(timeMillis); // 设置结束时间为昨天的同一时间
            }
            orders.setEnd_time(end_time);
            save(orders);
            //将预约用户id和充电桩状态更新到充电桩表中
            chargingPortService.portUpdate(orders.getPort_id(), orders.getUser_id());
            // 延迟检测订单状态消息
            try {
                MultiDelayMessage<Integer> msg = MultiDelayMessage.of(orders.getId(), 5000L, 10000L, 15000L,30000L);
                rabbitTemplate.convertAndSend(
                        MqConstants.DELAY_EXCHANGE, MqConstants.DELAY_ORDER_ROUTING_KEY, msg,
                        new DelayMessageProcessor(msg.removeNextDelay().intValue())
                );
            } catch (AmqpException e) {
                log.error("延迟消息发送异常！", e);
            }
        }
    }

    // 取消订单
    public void cancelOrder(Integer id) {
        // 1.取消订单
        lambdaUpdate()
                .set(Orders::getStatus, "已取消")
                .set(Orders::getUpdate_time, LocalDateTime.now())
                .eq(Orders::getId, id)
                .update();
        // 2.恢复充电桩状态和默认user_id
        Orders orders = getById(id);
        chargingPortService.changePort(orders.getPort_id());
    }

    // 通过user_id和status取消订单
    private void cancel(Integer user_id, String status) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .set(Orders::getStatus, "已取消")
                .set(Orders::getUpdate_time, LocalDateTime.now())
                .eq(Orders::getUser_id, user_id)
                .eq(Orders::getStatus, status);

        Orders orders = getOne(new QueryWrapper<Orders>().eq("user_id", user_id).eq("status", status));
        if (orders != null) {
            update(updateWrapper);
            chargingPortService.changePort(orders.getPort_id());
        } else {
            throw new RuntimeException("未找到该用户的订单！");
        }
    }

    public void cancelOrderByUserId(Integer user_id) {
        cancel(user_id, "待付款");
    }
    public void cancelBookOrder(Integer user_id) {
        cancel(user_id, "已预约");
    }
    // 订单完成
    public void endOrder(Integer user_id) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<Orders>()
                .set(Orders::getStatus, "已结束")
                .set(Orders::getUpdate_time, LocalDateTime.now())
                .eq(Orders::getUser_id, user_id)
                .eq(Orders::getStatus, "使用中");
        Orders orders = getOne(new QueryWrapper<Orders>().eq("user_id", user_id).eq("status", "使用中"));
        if (orders != null) {
            update(updateWrapper);
            chargingPortService.changePort(orders.getPort_id());
        } else {
            throw new RuntimeException("未找到该用户的订单！");
        }
    }

    // 查询用户的订单信息
    public List<Orders> getInfo(Integer user_id) {
        return ordersMapper.getInfo(user_id);
    }

    // 订单完成
    public void successOrder(Integer id) {
        // 1.修改订单状态
        lambdaUpdate()
                .set(Orders::getStatus, "已结束")
                .set(Orders::getUpdate_time, LocalDateTime.now())
                .eq(Orders::getId, id)
                .update();
        // 2.2.恢复充电桩状态和默认user_id
        Orders orders = getById(id);
        chargingPortService.changePort(orders.getPort_id());
    }

}
