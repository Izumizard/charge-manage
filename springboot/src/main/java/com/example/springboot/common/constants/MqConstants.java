package com.example.springboot.common.constants;

public interface MqConstants {
    //延迟交换机
    String DELAY_EXCHANGE = "trade.delay.topic";
    //延迟队列
    String DELAY_ORDER_QUEUE = "trade.order.delay.queue";
    //延迟路径键
    String DELAY_ORDER_ROUTING_KEY = "order.query";

}

