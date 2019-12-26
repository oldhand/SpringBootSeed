package com.github.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 说明: topic交换机配置
 */
@Configuration
public class TopicExchangeConfig {
    /**
     * 声明队列
     *
     * @return
     */
    @Bean
    public Queue httpRequestQueue() {
        return new Queue(RabbitConstant.MESSAGE_QUEUE, true);
    }
    /**
     * 声明交换机
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(RabbitConstant.CONTROL_EXCHANGE);
    }

    @Bean
    public Binding bindingHttpRequest() {
        return BindingBuilder.bind(httpRequestQueue()).to(topicExchange()).with(RabbitConstant.MESSAGE_ROUTING_KEY);
    }
}
