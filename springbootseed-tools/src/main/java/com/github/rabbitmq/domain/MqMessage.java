package com.github.rabbitmq.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Getter
@Setter
@ApiModel
public class MqMessage {
    @NotBlank
    @ApiModelProperty("队列名称")
    private String name;

    @NotBlank
    @ApiModelProperty("消息体 (json字符串)")
    private String message;

    @ApiModelProperty("唯一键 (键值相同时，判断为重复提交,为空时自动创建)")
    private String uniquekey;

    @ApiModelProperty("是否锁定 (0：并发，1，单例)")
    private Integer islock;

    @ApiModelProperty("是否同步 (0：异步，1：同步)")
    private Integer isasync;

    @Override
    public String toString() {
        return "{name=" + name  + ",uniquekey=" + uniquekey  + ",islock=" + islock  + ",isasync=" + isasync  + ", message=" + message  + "}";
    }
}
