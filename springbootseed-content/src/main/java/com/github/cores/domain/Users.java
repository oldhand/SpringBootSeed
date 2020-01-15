package com.github.cores.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.base.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author oldhand
* @date 2020-01-02
*/
@Entity
@Data
@Table(name="base_users")
public class Users extends BaseEntity implements Serializable {
	
    // 用户ID
    @Column(name = "profileid",unique = true)
	@ApiModelProperty("用户ID")
	private String profileid;

    // saasid
    @Column(name = "saasid")
    @ApiModelProperty("saasid")
    private long saasid;

    // 编号
    @Column(name = "users_no")
	@ApiModelProperty("编号")
	private String usersNo;
	
    // 排序号
    @Column(name = "sequence")
	@ApiModelProperty("排序号")
	private String sequence;
	
    // 国家代码
    @Column(name = "regioncode")
	@ApiModelProperty("国家代码")
	private String regioncode;
	
    // 手机号码
    @Column(name = "mobile")
	@ApiModelProperty("手机号码")
	private String mobile;
	
    // 昵称
    @Column(name = "givenname")
	@ApiModelProperty("昵称")
	private String givenname;
	
    // 用户状态
    @Column(name = "status")
	@ApiModelProperty("用户状态")
	private String status;

    // 部门ID
    @Column(name = "deptid")
    @ApiModelProperty("部门ID")
    private long deptid;

    // 权限ID
    @Column(name = "permissionid")
    @ApiModelProperty("权限ID")
    private long permissionid;

    // 是否管理员
    @Column(name = "isadmin",nullable = false)
    @ApiModelProperty("是否管理员")
    private Integer isadmin;
	
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
	

    public void copy(Users source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}