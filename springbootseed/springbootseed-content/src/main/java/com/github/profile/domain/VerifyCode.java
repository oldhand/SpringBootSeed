package com.github.profile.domain;

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
public class VerifyCode {
	// 验证码
	@ApiModelProperty("验证码")
	private String verifycode;

	// UUID
	@ApiModelProperty("UUID")
	private String uuid;

    @Override
    public String toString() {
        return "{verifycode=" + verifycode  + ", uuid=" + uuid  + "}";
    }
}