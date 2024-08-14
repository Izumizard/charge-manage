package com.example.springboot.listeners;

import com.example.springboot.common.constants.MqConstants;
import com.example.springboot.common.mq.DelayMessageProcessor;
import com.example.springboot.controller.dto.MultiDelayMessage;
import com.example.springboot.entity.Balance;
import com.example.springboot.entity.Orders;
import com.example.springboot.mapper.BalanceMapper;
import com.example.springboot.service.BalanceService;
import com.example.springboot.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 功能：订单状态监视器
 * 日期：2024/1/9 16:25
 */
@Component
@RequiredArgsConstructor
public class OrderStatusCheckListener {
    @Resource
    OrdersService ordersService;
    @Resource
    RabbitTemplate rabbitTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConstants.DELAY_ORDER_QUEUE, durable = "true"),
            exchange = @Exchange(value = MqConstants.DELAY_EXCHANGE, delayed = "true", type = ExchangeTypes.TOPIC),
            key = MqConstants.DELAY_ORDER_ROUTING_KEY
    ))
    public void listenOrderDelayMessage(MultiDelayMessage<Long> msg) {
        System.out.println("开始查询订单状态");
        // 1.查询订单状态
        Orders orders = ordersService.getById(msg.getData());
        // 2.判断是否已经支付
        if (orders == null || !Objects.equals(orders.getStatus(), "待付款")) {
            // 订单不存在或者已经被处理
            return;
        }

        // 3.判断是否存在延迟时间
        if (msg.hasNextDelay()) {
            // 4.1.存在，重发延迟消息
            Long nextDelay = msg.removeNextDelay();
            rabbitTemplate.convertAndSend(
                    MqConstants.DELAY_EXCHANGE, MqConstants.DELAY_ORDER_ROUTING_KEY,
                    msg, new DelayMessageProcessor(nextDelay.intValue()));
            return;
        }
        // 4.不存在，取消订单
        ordersService.cancelOrder(orders.getId());
        System.out.println("订单已取消");
    }
}
