package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-02
*/
@Data
public class UsersDTO extends BaseDTO implements Serializable {
	
    // 用户ID
    private String profileid;

    // saasid
    private Long saasid;
	
    // 编号
    private String usersNo;
	
    // 排序号
    private Integer sequence;
	
    // 国家代码
    private String regioncode;
	
    // 手机号码
    private String mobile;
	
    // 昵称
    private String givenname;
	
    // 用户状态
    private Boolean status;

    // 部门ID
    private long deptid;

    // 权限ID
    private long permissionid;

    // 是否管理员
    private Boolean isadmin;
	
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
	
}