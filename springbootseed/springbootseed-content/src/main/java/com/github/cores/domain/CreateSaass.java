package com.github.cores.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Getter
@Setter
@ApiModel
public class CreateSaass {
    // 名称
    @ApiModelProperty("名称")
    private String name;

    // 账户
    @ApiModelProperty("账户")
    private String username;

    // 公司名称
    @ApiModelProperty("公司名称")
    private String companyname;

    // 短名称
    @ApiModelProperty("短名称")
    private String shortname;

    // 省份
    @ApiModelProperty("省份")
    private String province;

    // 城市
    @ApiModelProperty("城市")
    private String city;

    // 联系人
    @ApiModelProperty("联系人")
    private String contact;

    // 联系电话
    @ApiModelProperty("国家代码")
    private String regioncode;

    // 联系电话
    @ApiModelProperty("联系电话")
    private String mobile;

    // 联系电话
    @ApiModelProperty("密码")
    private String password;

    @Override
    public String toString() {
        return "{name=" + name  + ", " +
                "username=" + username  + ", " +
                "companyname=" + companyname  + ", " +
                "shortname=" + shortname  + ", " +
                "province=" + province  + ", " +
                "city=" + city  + ", " +
                "contact=" + contact  + ", " +
                "regioncode=" + regioncode  + ", " +
                "mobile=" + mobile  + ", " +
                "password=******}";
    }
}
