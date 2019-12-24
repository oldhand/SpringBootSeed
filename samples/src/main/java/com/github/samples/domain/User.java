package com.github.samples.domain;

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
* @date 2019-12-23
*/
@Entity
@Data
@Table(name="user")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
	private Long id;

    // 创建时间
    @Column(name = "create_time")
	@ApiModelProperty("创建时间")
	private Timestamp createTime;

    @Column(name = "email")
	private String email;

    @Column(name = "enabled",nullable = false)
	private Boolean enabled;

    @Column(name = "last_password_reset_time")
	private Timestamp lastPasswordResetTime;

    @Column(name = "password")
	private String password;

    @Column(name = "phone")
	private String phone;

    // 用户名
    @Column(name = "username",unique = true)
	@ApiModelProperty("用户名")
	private String username;

    @Column(name = "dept_id")
	private Long deptId;

    @Column(name = "job_id")
	private Long jobId;

    @Column(name = "avatar_id")
	private Long avatarId;

    public void copy(User source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}