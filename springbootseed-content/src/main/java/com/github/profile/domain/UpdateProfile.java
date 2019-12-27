package com.github.profile.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * @author oldhand
 * @date 2019-12-27
 */
@Getter
@Setter
@ApiModel
public class UpdateProfile {

    // 用户名称
    @NotBlank
    @ApiModelProperty("用户名称")
    private String username;

    // 国家代码
    @NotBlank
    @ApiModelProperty("国家代码")
    private String regioncode;

    // 手机号码
    @NotBlank
    @ApiModelProperty("手机号码")
    private String mobile;

    // 昵称
    @ApiModelProperty("昵称")
    private String givenname;

    // 用户邮箱
    @ApiModelProperty("用户邮箱")
    private String email;

    // 用户头像地址
    @ApiModelProperty("用户头像地址")
    private String link;

    // 性别
    @ApiModelProperty("性别")
    private String gender;

    // 所属国家
    @ApiModelProperty("所属国家")
    private String country;

    // 地区
    @ApiModelProperty("地区")
    private String region;

    // 出生日期
    @ApiModelProperty("出生日期")
    private String birthdate;

    // 省份
    @ApiModelProperty("省份")
    private String province;

    // 城市
    @ApiModelProperty("城市")
    private String city;

    // 真实姓名
    @ApiModelProperty("真实姓名")
    private String realname;

    // 身份证号码
    @ApiModelProperty("身份证号码")
    private String identitycard;

    // 注册IP
    @ApiModelProperty("注册IP")
    private String regIp;

    // 注册系统
    @ApiModelProperty("注册系统")
    private String system;

    // 注册浏览器
    @ApiModelProperty("注册浏览器")
    private String browser;

    @Override
    public String toString() {
        return "{username=" + username  + ", password= ******}";
    }
}