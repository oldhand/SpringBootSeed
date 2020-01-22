package com.github.sms.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


/**
* @author oldhand
* @date 2020-01-21
*/
@Data
public class SmslogDTO implements Serializable {

    private Long id;

    // 创建日期
    protected Timestamp published;

    // 发送人
    protected String profileid;
    // 云服务ID
    private Long saasid;

    private String template;
	
    // 手机号码
    private String mobile;
	
    // 国际区号
    private String regioncode;
	
    // 验证码
    private String verifycode;

    private String uuid;

    private Integer status;
	
    // 返回值
    private String result;
	
}