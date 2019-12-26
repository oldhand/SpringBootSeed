package com.github.rabbitmq.producer;

import com.github.rabbitmq.config.RabbitConstant;
import com.github.rabbitmq.service.dto.MqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 说明: 生产者
 */

@Slf4j
@Component
public class Sender {
    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送信息到message队列
     *
     * @param mq
     */
    public String sendMessage(MqDTO mq) {
        Map<String, String> keyMap = new HashMap<String, String>(3);
        keyMap.put("id", mq.getId().toString());
        keyMap.put("name", mq.getName());
        keyMap.put("message",  mq.getMessage());
        if (mq.getIslock() == 0) {
            if (mq.getIsasync() == 0) {
                send(keyMap, RabbitConstant.CONTROL_EXCHANGE, RabbitConstant.MESSAGE_ROUTING_KEY);
                return "";
            }
            else {
                return sendAndReceive(keyMap, RabbitConstant.CONTROL_EXCHANGE, RabbitConstant.ASYNC_MESSAGE_ROUTING_KEY);
            }
        }
        else {
            if (mq.getIsasync() == 0) {
                send(keyMap, RabbitConstant.CONTROL_EXCHANGE, RabbitConstant.LOCK_MESSAGE_ROUTING_KEY);
                return "";
            }
            else {
                return sendAndReceive(keyMap, RabbitConstant.CONTROL_EXCHANGE, RabbitConstant.ASYNC_LOCK_MESSAGE_ROUTING_KEY);
            }
        }
    }

    /**
     * 发送消息(异步)
     *
     * @param msg        消息
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    private void send(Map<String, String> msg, String exchange, String routingKey) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送异步消息:{}", msg);
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationId);
    }

    /**
     * 发送并接收消息(阻塞)
     *
     * @param msg        消息
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    private String sendAndReceive(Map<String, String> msg, String exchange, String routingKey) {
        try {
            CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
            log.info("发送阻塞消息:{}", msg);
            //转换并发送消息,且等待消息者返回响应消息。
            Object response = rabbitTemplate.convertSendAndReceive(exchange, routingKey, msg, correlationId);
            if (response != null) {
                log.info("消费者响应:{}", response.toString());
            }
            return response.toString();
        }
        catch (Exception ee) {
            return "Failed to send message";
        }
    }
}
