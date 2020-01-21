package com.github.rabbitmq.config;

/**
 * 说明: Rabbit常量
 */
public class RabbitConstant {
    /**
     * 并发队列
     */
    public static final String MESSAGE_QUEUE = "message";
    /**
     * 并发队列路由键
     */
    public static final String MESSAGE_ROUTING_KEY = "message.key";

    /**
     * 单例队列
     */
    public static final String LOCK_MESSAGE_QUEUE = "lockmessage";
    /**
     * 单例队列路由键
     */
    public static final String LOCK_MESSAGE_ROUTING_KEY = "lockmessage.key";

    /**
     * 阻塞队列
     */
    public static final String ASYNC_MESSAGE_QUEUE = "asyncmessage";
    /**
     * 阻塞队列路由键
     */
    public static final String ASYNC_MESSAGE_ROUTING_KEY = "asyncmessage.key";

    /**
     * 阻塞单例队列
     */
    public static final String ASYNC_LOCK_MESSAGE_QUEUE = "asynclockmessage";
    /**
     * 阻塞单例队列路由键
     */
    public static final String ASYNC_LOCK_MESSAGE_ROUTING_KEY = "asynclockmessage.key";
    /**
     * 交换机
     */
    public static final String CONTROL_EXCHANGE = "control.exchange";

    public static final String CONCURRENCY = "10";
}
