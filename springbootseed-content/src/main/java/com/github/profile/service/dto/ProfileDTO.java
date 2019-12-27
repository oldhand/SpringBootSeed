package com.github.profile.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author oldhand
* @date 2019-12-27
*/
@Data
public class ProfileDTO implements Serializable {

    private Long identifier;

    // 用户ID
    private String id;

    // 用户名称
    private String username;

    // 创建日期
    private Timestamp published;

    // 更新日期
    private Timestamp updated;

    // 用户类型
    private String type;

    // 国家代码
    private String regioncode;

    // 手机号码
    private String mobile;

    // 密码
    private String password;

    // 昵称
    private String givenname;

    // 用户状态
    private Integer status;

    // 用户邮箱
    private String email;

    // 用户头像地址
    private String link;

    // 性别
    private String gender;

    // 所属国家
    private String country;

    // 地区
    private String region;

    // 出生日期
    private String birthdate;

    // 省份
    private String province;

    // 城市
    private String city;

    // 真实姓名
    private String realname;

    // 身份证号码
    private String identitycard;

    // 注册IP
    private String regIp;

    // 注册系统
    private String system;

    // 注册浏览器
    private String browser;

}