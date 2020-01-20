package com.github.rabbitmq.consumer;

import com.github.config.thread.ThreadPoolExecutorUtil;
import com.github.rabbitmq.config.RabbitConstant;
import com.github.rabbitmq.service.MqService;
import com.github.rabbitmq.service.dto.MqDTO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.*;

import static com.github.rabbitmq.config.RabbitConstant.MESSAGE_QUEUE;

/**
 * 说明: 消费者
 */
@Slf4j
@Component
@Configuration
public class MessageReceiver {

    @Value("${rabbitmq.target-class}")
    private String targetClass;

    @Autowired
    private MqService mqService;


    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.MESSAGE_QUEUE,durable="true"),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.MESSAGE_ROUTING_KEY
            ),
            concurrency = RabbitConstant.CONCURRENCY
    )
    public void processMessage(Map<String, String> message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        log.info("收到队列消息:{}", message.toString());
        consumer(message);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.LOCK_MESSAGE_QUEUE,durable="true"),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.LOCK_MESSAGE_ROUTING_KEY
            ),
            concurrency =  "1"
    )
    public void processLockMessage(Map<String, String> message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        log.info("收到单例队列的消息:{}", message.toString());
        consumer(message);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.ASYNC_MESSAGE_QUEUE,durable="true"),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.ASYNC_MESSAGE_ROUTING_KEY
            ),
            concurrency =  RabbitConstant.CONCURRENCY
    )
    public  Map<String, String> processAsyncMessage(Map<String, String> message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        log.info("收到阻塞队列的消息:{}", message.toString());
        return consumer(message);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = RabbitConstant.ASYNC_LOCK_MESSAGE_QUEUE,durable="true"),
                    exchange = @Exchange(value = RabbitConstant.CONTROL_EXCHANGE),
                    key = RabbitConstant.ASYNC_LOCK_MESSAGE_ROUTING_KEY
            ),
            concurrency = "1"
    )
    public  Map<String, String> processAsyncLockMessage(Map<String, String> message, @Headers Map<String,Object> headers, Channel channel) throws Exception {
        log.info("收到阻塞单例队列的消息:{}", message.toString());
        return consumer(message);
    }

    public Map<String, String> consumer(Map<String, String> obj) {
        String id = obj.get("id");
        String name = obj.get("name");
        String message = obj.get("message");
        Map<String, String> result = new HashMap<String, String>(2);
        try {
            // 执行任务
            ConsumerRunnable task;
            if (name.equals("makeModEntityNo")) {
                task = new ConsumerRunnable("com.github.utils.MqUtils","run",message);
            }
            else {
                task = new ConsumerRunnable(targetClass,name,message);
            }
            Object res = task.call();
            result.put("ack", "2");
            result.put("result", res.toString());
        } catch (Exception e) {
            log.error("队列执行出错：{}",e.getMessage());
            result.put("ack", "3");
            result.put("result", e.getMessage());
        }
        try {
            mqService.updateAck(Long.parseLong(id),result.get("ack"),result.get("result"));
        } catch (Exception e) {
            log.error("保存队列执行结果出错：{}",e.toString());
        }
        return result;
    }

}

