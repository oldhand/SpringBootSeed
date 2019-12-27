package com.github.profile.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author oldhand
* @date 2019-12-27
*/
@Entity
@Data
@Table(name="profile")
public class Profile implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier")
    @ApiModelProperty("自增ID")
	private Long identifier;

    // 用户ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	@ApiModelProperty("用户ID")
	private String id;

    // 用户名称
    @Column(name = "username",unique = true,nullable = false)
	@ApiModelProperty("用户名称")
	private String username;

    // 创建日期
    @Column(name = "published",nullable = false)
	@ApiModelProperty("创建日期")
	private Timestamp published;

    // 更新日期
    @Column(name = "updated",nullable = false)
	@ApiModelProperty("更新日期")
	private Timestamp updated;

    // 用户类型
    @Column(name = "type",nullable = false)
	@ApiModelProperty("用户类型")
	private String type;

    // 国家代码
    @Column(name = "regioncode",nullable = false)
	@ApiModelProperty("国家代码")
	private String regioncode;

    // 手机号码
    @Column(name = "mobile",nullable = false)
	@ApiModelProperty("手机号码")
	private String mobile;

    // 密码
    @Column(name = "password",nullable = false)
	@ApiModelProperty("密码")
	private String password;

    // 昵称
    @Column(name = "givenname")
	@ApiModelProperty("昵称")
	private String givenname;

    // 用户状态
    @Column(name = "status")
	@ApiModelProperty("用户状态")
	private Integer status;

    // 用户邮箱
    @Column(name = "email")
	@ApiModelProperty("用户邮箱")
	private String email;

    // 用户头像地址
    @Column(name = "link")
	@ApiModelProperty("用户头像地址")
	private String link;

    // 性别
    @Column(name = "gender")
	@ApiModelProperty("性别")
	private String gender;

    // 所属国家
    @Column(name = "country")
	@ApiModelProperty("所属国家")
	private String country;

    // 地区
    @Column(name = "region")
	@ApiModelProperty("地区")
	private String region;

    // 出生日期
    @Column(name = "birthdate")
	@ApiModelProperty("出生日期")
	private String birthdate;

    // 省份
    @Column(name = "province")
	@ApiModelProperty("省份")
	private String province;

    // 城市
    @Column(name = "city")
	@ApiModelProperty("城市")
	private String city;

    // 真实姓名
    @Column(name = "realname")
	@ApiModelProperty("真实姓名")
	private String realname;

    // 身份证号码
    @Column(name = "identitycard")
	@ApiModelProperty("身份证号码")
	private String identitycard;

    // 注册IP
    @Column(name = "reg_ip")
	@ApiModelProperty("注册IP")
	private String regIp;

    // 注册系统
    @Column(name = "system")
	@ApiModelProperty("注册系统")
	private String system;

    // 注册浏览器
    @Column(name = "browser")
	@ApiModelProperty("注册浏览器")
	private String browser;

    public void copy(Profile source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}