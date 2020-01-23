package com.github.sms.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @author oldhand
* @date 2019-12-27
*/
@Getter
@Setter
@ApiModel
public class SmsVerifyCode {
	// 短信验证码
	@ApiModelProperty("短信验证码")
	private String verifycode;

	// UUID
	@ApiModelProperty("UUID")
	private String uuid;

	// 传递参数
	@ApiModelProperty("传递参数")
	private String parameter;

    @Override
    public String toString() {
        return "{verifycode=" + verifycode  + ", uuid=" + uuid  + ", parameter=" + parameter  + "}";
    }
}