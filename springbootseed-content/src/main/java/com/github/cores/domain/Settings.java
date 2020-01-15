package com.github.cores.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @author oldhand
* @date 2020-01-15
*/
@Entity
@Data
@Table(name="settings")
public class Settings implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 系统名称
    @Column(name = "name",nullable = false)
	@ApiModelProperty("系统名称")
	private String name;
	
    // 域名
    @Column(name = "site",nullable = false)
	@ApiModelProperty("域名")
	private String site;
	
    // 网站备案号
    @Column(name = "icp",nullable = false)
	@ApiModelProperty("网站备案号")
	private String icp;
	
    // Logo
    @Column(name = "logo")
	@ApiModelProperty("Logo")
	private String logo;
	
    // 版权公司
    @Column(name = "companyname",nullable = false)
	@ApiModelProperty("版权公司")
	private String companyname;
	
    // 是否单点登录
    @Column(name = "singlelogin",nullable = false)
	@ApiModelProperty("是否单点登录")
	private Boolean singlelogin;
	

    public void copy(Settings source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}