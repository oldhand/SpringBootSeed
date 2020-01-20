package com.github.profile.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
* @author oldhand
* @date 2019-12-27
*/
@Getter
@Setter
@ApiModel
public class LoginProfile {

	// 用户ID
	@ApiModelProperty("用户ID")
	private String id;

    // 密码
	@ApiModelProperty("密码")
	private String password;

	// 验证码
	@ApiModelProperty("验证码")
	private String verifycode;

	// UUID
	@ApiModelProperty("UUID")
	private String uuid;

    @Override
    public String toString() {
        return "{id=" + id  + ", password=******, verifycode=" + verifycode  + ", uuid=" + uuid  + "}";
    }
}