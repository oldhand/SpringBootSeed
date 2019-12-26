package com.github.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 说明: 交换机配置
 */
@Configuration
public class DirectRabbitConfig {
    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue message_queue() {
        return new Queue(RabbitConstant.MESSAGE_QUEUE, true);
    }
    @Bean
    public Queue lock_message_queue() {
        return new Queue(RabbitConstant.LOCK_MESSAGE_QUEUE, true);
    }
    @Bean
    public Queue async_message_queue() {
        return new Queue(RabbitConstant.ASYNC_MESSAGE_QUEUE, true);
    }
    @Bean
    public Queue async_lock_message_queue() {
        return new Queue(RabbitConstant.ASYNC_LOCK_MESSAGE_QUEUE, true);
    }
    /**
     * 声明交换机
     *
     * @return
     */
    @Bean
    public DirectExchange DirectExchange() {
        return new DirectExchange(RabbitConstant.CONTROL_EXCHANGE);
    }

    @Bean
    public Binding message_queue_binding() {
        return BindingBuilder.bind(message_queue()).to(DirectExchange()).with(RabbitConstant.MESSAGE_ROUTING_KEY);
    }
    @Bean
    public Binding lock_essage_queue_binding() {
        return BindingBuilder.bind(lock_message_queue()).to(DirectExchange()).with(RabbitConstant.LOCK_MESSAGE_ROUTING_KEY);
    }
    @Bean
    public Binding async_essage_queue_binding() {
        return BindingBuilder.bind(async_message_queue()).to(DirectExchange()).with(RabbitConstant.ASYNC_MESSAGE_ROUTING_KEY);
    }
    @Bean
    public Binding async_lock_essage_queue_binding() {
        return BindingBuilder.bind(async_lock_message_queue()).to(DirectExchange()).with(RabbitConstant.ASYNC_LOCK_MESSAGE_ROUTING_KEY);
    }

}
