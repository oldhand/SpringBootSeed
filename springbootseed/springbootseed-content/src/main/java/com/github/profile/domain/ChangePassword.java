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
public class ChangePassword {

	// 用户ID
	@ApiModelProperty("用户ID")
	private String profileid;

	// 密码
	@ApiModelProperty("新密码")
	private String password;

	// 验证码
	@ApiModelProperty("验证码")
	private String verifycode;

	// UUID
	@ApiModelProperty("UUID")
	private String uuid;

    @Override
    public String toString() {
        return "{profileid=" + profileid  + ", password=******, uuid=" + uuid  + ", verifycode=" + verifycode  + "}";
    }
}