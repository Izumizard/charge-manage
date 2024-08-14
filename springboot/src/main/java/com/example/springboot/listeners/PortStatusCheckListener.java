package com.example.springboot.listeners;

import com.example.springboot.common.constants.MqConstants;
import com.example.springboot.common.mq.DelayMessageProcessor;
import com.example.springboot.controller.dto.MultiDelayMessage;
import com.example.springboot.entity.Orders;
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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能：
 * 日期：2024/1/12 19:14
 */
@Component
@RequiredArgsConstructor
public class PortStatusCheckListener {
    @Resource
    OrdersService ordersService;
    @Resource
    RabbitTemplate rabbitTemplate;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "mark.port.check.queue", durable = "true"),
            exchange = @Exchange(value = "port.topic", delayed = "true", type = ExchangeTypes.TOPIC),
            key = "port.check"
    ))
    public void listenStatusDelayMessage(MultiDelayMessage<Long> msg){
        Orders orders = ordersService.getById(msg.getData());
        if (msg.hasNextDelay()) {
            Long nextDelay = msg.removeNextDelay();
            rabbitTemplate.convertAndSend(
                    "port.topic", "port.check",
                    msg, new DelayMessageProcessor(nextDelay.intValue()));
            return;
        }
        ordersService.successOrder(orders.getId());
        System.out.println("充电桩使用结束");
    }
}
