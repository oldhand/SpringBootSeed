package com.github.modules.security.security;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Getter
@Setter
@ApiModel
public class AuthApplication {

    @NotBlank
    @ApiModelProperty("应用ID")
    private String appid;

    @NotBlank
    @ApiModelProperty("密钥，32位字符串")
    private String secret;

    @ApiModelProperty("格林威治(GMT)时间戳，北京时间减8小时")
    private long timestamp;


    @Override
    public String toString() {
        return "{appid=" + appid  + ", secret= ******}";
    }
}
