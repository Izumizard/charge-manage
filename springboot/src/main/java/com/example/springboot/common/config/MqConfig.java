package com.example.springboot.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 功能：
 * 日期：2024/1/7 0:36
 */


@Configuration
@ConditionalOnClass(ObjectMapper.class)
@RequiredArgsConstructor
public class MqConfig {

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
//public class RabbitMQConfig {
//
//    //死信队列
//    private static final String DELAY_QUEUE = "delay_queue";
//    //正常队列
//    public static final String NORMAL_QUEUE = "normal_queue";
//    //死信交换器
//    private static final String DELAY_EXCHANGE = "delay_exchange";
//    //死信路由键
//    private static final String DELAY_KEY = "delay_key";
//
//    // 构建一个死信队列对象
//    @Bean
//    public Queue orderDelayQueue(){
//        //构造参数
//        Map<String,Object> arguments = new HashMap<>();
//        //为死信队列设置队列交换器：order.event.exchange
//        arguments.put("x-dead-letter-exchange","order.event.exchange");
//        //为死信队列设置队列路由key：order.release.order
//        arguments.put("x-dead-letter-routing-key","order.release.order");
//        //为死信队列中的消息设置过期时间：1 分钟
//        arguments.put("x-message-ttl",60000);
//        /**
//         *    String name, 队列名字
//         *    boolean durable, 队列是否持久化
//         *    boolean exclusive, 队列是否排他的
//         *    boolean autoDelete, 队列是否删除
//         *    @Nullable Map<String, Object> arguments 自定义属性
//         */
//        // 构建对象，塞入相关参数
//        Queue queue = new Queue("order.delay.queue",
//                true,
//                false,
//                false,
//                arguments
//        );
//        return new Queue(DELAY_QUEUE, true);
//    }
//
//    //构建普通的队列
//    @Bean
//    public Queue orderReleaseOrderQueue(){
//        // 构建对象，塞入相关参数
//        Queue queue = new Queue("order.release.order.queue",
//                true,
//                false,
//                false,
//                null
//        );
//        return queue;
//    }
//
//    // 构建交换机对象
//    @Bean
//    public Exchange orderEventExchange(){
//        /**
//         * String name, 交换机名称
//         * boolean durable, 队列是否持久化
//         * boolean autoDelete, 队列是否删除
//         */
//        TopicExchange exchange = new TopicExchange(
//                "order.event.exchange",
//                true,
//                false);
//        return exchange;
//    }
//
//    // 将交换机与死信队列绑定
//    @Bean
//    public Binding orderCreateOrderBinding(){
//        /**
//         * String destination, 目的地
//         * DestinationType destinationType, 目的地类型
//         * String exchange, 交换机
//         * String routingKey, 路由key
//         * @Nullable Map<String, Object> arguments 参数
//         */
//        Binding binding = new Binding("order.delay.queue",
//                Binding.DestinationType.QUEUE,
//                "order.event.exchange",
//                "order.create.order",
//                null
//        );
//        return binding;
//    }
//
//    // 将交换机与普通队列绑定
//    @Bean
//    public Binding orderReleaseOrderBinding(){
//        /**
//         * String destination, 目的地
//         * DestinationType destinationType, 目的地类型
//         * String exchange, 交换机
//         * String routingKey, 路由key
//         * @Nullable Map<String, Object> arguments 参数
//         */
//        Binding binding = new Binding("order.release.order.queue",
//                Binding.DestinationType.QUEUE,
//                "order.event.exchange",
//                "order.release.order",
//                null
//        );
//        return binding;
//    }
//
//    // 创建一个正常队列
//    @Bean
//    public Queue orderNormalQueue() {
//        return new Queue(NORMAL_QUEUE, true);
//    }
//
//    // 创建一个延迟交换器
//    @Bean
//    public TopicExchange delayExchange() {
//        return new TopicExchange(DELAY_EXCHANGE);
//    }
//
//    // 延迟队列绑定到延迟交换器，并设置路由键为延迟键
//    @Bean
//    public Binding delayBinding() {
//        return BindingBuilder.bind(orderDelayQueue()).to(delayExchange()).with(DELAY_KEY);
//    }
//
//    // 正常队列绑定到延迟交换器，并设置路由键为延迟键
//    @Bean
//    public Binding normalBinding() {
//        return BindingBuilder.bind(orderNormalQueue()).to(delayExchange()).with(DELAY_KEY);
//    }
//
//}

