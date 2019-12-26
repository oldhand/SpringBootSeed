package com.github.rabbitmq.core;


import com.github.rabbitmq.config.RabbitConfig;
import com.github.rabbitmq.config.RabbitConstant;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import java.text.MessageFormat;

import static com.github.rabbitmq.config.RabbitConstant.MESSAGE_QUEUE;


/**
 * 说明: 消费者
 */
@Slf4j
@Component
public class Receiver {

    @Autowired
    private RabbitConfig rabbitConfig;




    /**
     * 处理队列消息
     *
     */
//    @RabbitListener(queues = MESSAGE_QUEUE, concurrency =  "10")
//    public String processMessage(String msg) throws Exception {
//        String response = MessageFormat.format("_______________收到{0}队列的消息:{1}_______________", MESSAGE_QUEUE, msg);
//        Thread.sleep(1000);
//        return response.toUpperCase();
//    }

    // args 接收的参数，对应生产者传递的类型,  channel 接口可用来发送消息或确认消息被消费， @Header 获取头信息, tag表示该消息index
//    public void receiveMsg(Object args, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        //处理args消息信息过程
//
//        // 下面是手动消息应答
//        // channel.basicAck(tag, false);  作用:消息确认接收，参数2是否批量，true表示一次性ack所有小于tag的消息
//        // channel.basicNack(tag, false, true) 作用:拒绝或重新入队列, 参数2是否批量同上，参数3被拒绝的是否重新入队列
//        // channel.basicReject(tag, false) 作用:拒绝或重新入队列(只能一次拒绝一条消息)，参数2被拒绝的是否重新入队列
//    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.MESSAGE_QUEUE),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.MESSAGE_ROUTING_KEY
            ),
            concurrency =  "1"
    )
    public void process1(Message message) throws Exception {
        String response = MessageFormat.format("_______________收到{0}队列的消息:{1}_______________", MESSAGE_QUEUE, new String(message.getBody()));
        System.out.println(response);
        Thread.sleep(1000);
    }
}
