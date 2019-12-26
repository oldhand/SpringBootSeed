package com.github.rabbitmq.consumer;

import com.github.rabbitmq.config.RabbitConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.Map;

import static com.github.rabbitmq.config.RabbitConstant.MESSAGE_QUEUE;

/**
 * 说明: 消费者
 */
@Slf4j
@Component
public class MessageReceiver {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.MESSAGE_QUEUE,durable="true"),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.MESSAGE_ROUTING_KEY
            ),
            concurrency =  "15"
    )
    public void processMessage(Map<String, String> message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        String response = MessageFormat.format("_______________收到{0}队列的消息:{1}_______________", MESSAGE_QUEUE, new String(message.toString()));
        System.out.println(response);
        Thread.sleep(1000);
    }

}

