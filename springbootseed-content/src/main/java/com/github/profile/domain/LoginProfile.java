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

    // 用户名称
	@ApiModelProperty("用户名称")
	private String username;

    // 用户类型
    @NotBlank
	@ApiModelProperty("用户类型")
	private String type;

    // 国家代码
	@ApiModelProperty("国家代码")
	private String regioncode;

    // 手机号码
	@ApiModelProperty("手机号码")
	private String mobile;

    // 密码
    @NotBlank
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
        return "{username=" + username  + ", password= ******}";
    }
}