package com.github.rabbitmq.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2019-12-25
*/
@Data
public class MqDTO implements Serializable {

    // 队列ID
    // 处理精度丢失问题
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    // 创建日期
    private Timestamp published;

    // 队列名称
    private String name;

    // 消息体
    private String message;

    // 消息体
    private String result;

    // 响应代码
    private Integer ack;

    // 响应时间
    private Timestamp acktime;

    // 唯一键
    private String uniquekey;

    // 是否锁定
    private Integer islock;

    // 是否同步
    private Integer isasync;
}