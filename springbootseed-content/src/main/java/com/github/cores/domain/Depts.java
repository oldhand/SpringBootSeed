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
@Table(name="base_depts")
public class Depts extends BaseEntity implements Serializable {
	
    // 部门名称
    @Column(name = "deptname",nullable = false)
	@ApiModelProperty("部门名称")
	private String deptname;
	
    // 部门ID
    @Column(name = "deptid",nullable = false)
	@ApiModelProperty("部门ID")
	private String deptid;
	
    // 父部门ID
    @Column(name = "parentid",nullable = false)
	@ApiModelProperty("父部门ID")
	private String parentid;
	
    // 排序号
    @Column(name = "sequence",nullable = false)
	@ApiModelProperty("排序号")
	private Integer sequence;
	
    // 部门级别
    @Column(name = "depth",nullable = false)
	@ApiModelProperty("部门级别")
	private Integer depth;
	
    // SaaS
    @Column(name = "supplierid",nullable = false)
	@ApiModelProperty("SaaS")
	private Long supplierid;
	

    public void copy(Depts source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}