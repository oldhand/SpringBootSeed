package com.github.sms.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author oldhand
* @date 2020-01-21
*/
@Entity
@Data
@Table(name="smslog")
public class Smslog implements Serializable {

    //记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable=false, updatable=false)
    @ApiModelProperty("记录ID")
    private Long id;

    // 创建日期
    @Column(name = "published",insertable=false, updatable=false,nullable = false)
    @ApiModelProperty("创建日期")
    @JsonIgnore
    @CreationTimestamp
    protected Timestamp published;

    // 发送人
    @Column(name = "profileid",updatable=false,nullable = false)
    @ApiModelProperty("发送人")
    @JsonIgnore
    protected String profileid;

    // 云服务ID
    @Column(name = "saasid",updatable=false,nullable = false)
	@ApiModelProperty("云服务ID")
    @JsonIgnore
	private Long saasid;


    // 模板
    @Column(name = "template",updatable=false,nullable = false)
    @ApiModelProperty("模板")
    private String template;

    // 手机号码
    @Column(name = "mobile",updatable=false,nullable = false)
	@ApiModelProperty("手机号码")
	private String mobile;
	
    // 国际区号
    @Column(name = "regioncode",updatable=false,nullable = false)
	@ApiModelProperty("国际区号")
	private String regioncode;
	
    // 验证码
    @Column(name = "verifycode",updatable=false,nullable = false)
	@ApiModelProperty("验证码")
    @JsonIgnore
	private String verifycode;

    // UUID
    @Column(name = "uuid",nullable = false)
    @ApiModelProperty("UUID")
    @JsonIgnore
    private String uuid;

    // 状态
    @Column(name = "status",nullable = false)
    @ApiModelProperty("状态")
    @JsonIgnore
    private Integer status;
	
    // 返回值
    @Column(name = "result")
	@ApiModelProperty("返回值")
    @JsonIgnore
	private String result;
	

    public void copy(Smslog source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}