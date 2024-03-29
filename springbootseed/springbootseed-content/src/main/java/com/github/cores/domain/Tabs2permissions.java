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
* @date 2020-01-14
*/
@Entity
@Data
@Table(name="base_tabs2permissions")
public class Tabs2permissions extends BaseEntity implements Serializable {
	
    @Column(name = "saasid",nullable = false)
	private Long saasid;
	
    // 权限ID
    @Column(name = "permissionid",nullable = false)
	@ApiModelProperty("权限ID")
	private Long permissionid;
	
    // 模块ID
    @Column(name = "tabid",nullable = false)
	@ApiModelProperty("模块ID")
	private Integer tabid;
	
    // 全部权限
    @Column(name = "all_permission",nullable = false)
	@ApiModelProperty("全部权限")
	private Boolean all;
	
    // 编辑权限
    @Column(name = "edit_permission",nullable = false)
	@ApiModelProperty("编辑权限")
	private Boolean edit;
	
    // 删除权限
    @Column(name = "delete_permission",nullable = false)
	@ApiModelProperty("删除权限")
	private Boolean delete;
	
    // 查询权限
    @Column(name = "query_permission",nullable = false)
	@ApiModelProperty("查询权限")
	private Boolean query;
	
    // 新增权限
    @Column(name = "add_permission",nullable = false)
	@ApiModelProperty("新增权限")
	private Boolean add;
	

    public void copy(Tabs2permissions source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}