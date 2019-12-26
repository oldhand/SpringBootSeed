package com.github.rabbitmq.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author oldhand
* @date 2019-12-25
*/
@Entity
@Data
@Table(name="mq")
public class Mq implements Serializable {

    // 队列ID
    @Id
    @Column(name = "id",insertable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@ApiModelProperty("队列ID")
	private Long id;

    // 创建日期
    @Column(name = "published",insertable=false, updatable=false,nullable = false)
	@ApiModelProperty("创建日期")
    @CreationTimestamp
	private Timestamp published;

    // 队列类型
    @Column(name = "name",updatable=false,nullable = false)
	@ApiModelProperty("队列名称")
	private String name;

    // 消息体
    @Column(name = "message",updatable=false,nullable = false)
	@ApiModelProperty("消息体")
	private String message;

    // 响应结果
    @Column(name = "result")
    @ApiModelProperty("响应结果")
    private String result;

    // 响应代码
    @Column(name = "ack",nullable = false)
	@ApiModelProperty("响应代码")
	private Integer ack;

    // 响应时间
    @Column(name = "acktime")
	@ApiModelProperty("响应时间")
	private Timestamp acktime;

    // 唯一键
    @Column(name = "uniquekey",unique = true,updatable=false,nullable = false)
	@ApiModelProperty("唯一键")
	private String uniquekey;

    // 是否锁定
    @Column(name = "islock",updatable=false,nullable = false)
	@ApiModelProperty("是否锁定")
	private Integer islock;

    // 是否同步
    @Column(name = "isasync",updatable=false,nullable = false)
	@ApiModelProperty("是否同步")
	private Integer isasync;

    public void copy(Mq source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}