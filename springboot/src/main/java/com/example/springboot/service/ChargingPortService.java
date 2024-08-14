package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.mq.DelayMessageProcessor;
import com.example.springboot.controller.dto.MultiDelayMessage;
import com.example.springboot.entity.ChargingPort;
import com.example.springboot.entity.Orders;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.ChargingPortMapper;
import com.example.springboot.mapper.OrdersMapper;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 功能：
 * 日期：2023/11/21 0:14
 */
@Service
public class ChargingPortService extends ServiceImpl<ChargingPortMapper, ChargingPort> {

    @Resource
    OrdersMapper ordersMapper;
    @Resource
    ChargingPortMapper chargingPortMapper;
    @Resource
    RabbitTemplate rabbitTemplate;

    //查询同一station_id下的全部充电桩
    public List<ChargingPort> findByStationId(Integer station_id) {
        QueryWrapper<ChargingPort> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("station_id",station_id);
        return list(queryWrapper);
    }

    //下单前检查充电桩状态
    public String checkStatus(Integer port_id) {
        ChargingPort chargingPort = chargingPortMapper.selectById(port_id);
        return chargingPort.getPort_status();
    }

    //下单时更新充电桩的状态和使用者
    public void portUpdate(Integer port_id, Integer user_id) {
        ChargingPort chargingPort = chargingPortMapper.selectById(port_id);
        if (!"空闲中".equals(chargingPort.getPort_status())) {
            throw new ServiceException("300", "充电桩已被预约！");
        } else {
            lambdaUpdate()
                    .set(ChargingPort::getPort_status,"已预约")
                    .set(ChargingPort::getUser_Id, user_id)
                    .eq(ChargingPort::getId, port_id)
                    .update();
        }
    }

    //取消订单时、订单完成时更新充电桩的状态和使用者
    public void changePort(Integer port_id) {
        lambdaUpdate()
                .set(ChargingPort::getPort_status,"空闲中")
                .set(ChargingPort::getUser_Id, 0)
                .eq(ChargingPort::getId, port_id)
                .update();
    }


    /**1.据订单中的时间判断是否到达可使用时间
     * 2.根据user_id和port表中的user_id判断是否为订单用户
     * 3.创建一个Queue 用于执行订单状态改变(根据OrdersRequest中的duration判断执行时长)
     */

    public void useOrder(String port_number) {
        // 根据 port_number 查询 port 表中的 port_id
        ChargingPort port = chargingPortMapper.selectOne(new QueryWrapper<ChargingPort>()
                .eq("port_number", port_number));
        if (port != null) {
            // 1.当前时间是否在订单的预约时间范围内
            LocalDateTime currentTime = LocalDateTime.now();
            Orders orders = ordersMapper.selectOne(new QueryWrapper<Orders>()
                    .eq("port_id", port.getId())
                    .eq("status", "已预约"));
            if (orders == null) {
                throw new ServiceException("400", "暂无订单");
            }
            LocalDateTime startTime = orders.getStart_time().toLocalDateTime();
            LocalDateTime endTime = orders.getEnd_time().toLocalDateTime();
            if (currentTime.isBefore(startTime)) {
                throw new ServiceException("400", "未到预约时间");
            }  if (currentTime.isAfter(endTime)) {
                orders.setStatus("已结束");
                orders.setUpdate_time(Timestamp.valueOf(LocalDateTime.now()));
                port.setUser_Id(0);
                port.setPort_status("空闲中");
                chargingPortMapper.updateById(port);
                ordersMapper.updateById(orders);
                throw new ServiceException("400", "预约时间已结束");
            }
            if (!orders.getUser_id().equals(port.getUser_Id())) {
                throw new ServiceException("400", "你没在该时段预约！");
            } else {
                try {
                    orders.setStatus("使用中");
                    port.setPort_status("充电中");
                    chargingPortMapper.updateById(port);
                    ordersMapper.updateById(orders);
                    /*
                    项目用 1 min = 60000 ms
                    long delayTime = orders.getDuration() * 60 * 60 *1000L;
                    MultiDelayMessage<Long> msg = MultiDelayMessage.of(orders.getId(), delayTime);
                     */
                    //演示用
                    MultiDelayMessage<Integer> msg = MultiDelayMessage.of(orders.getId(), 5000L, 10000L, 15000L,30000L);
                    rabbitTemplate.convertAndSend(
                            "port.topic", "port.check", msg,
                            new DelayMessageProcessor(msg.removeNextDelay().intValue())
                    );
                } catch (AmqpException e) {
                    log.error("延迟消息发送异常！", e);
                }
            }
        }
    }
}
