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
@Table(name="base_roles")
public class Roles extends BaseEntity implements Serializable {
	
    @Column(name = "saasid",nullable = false)
	private Long saasid;
	
    // 角色名称
    @Column(name = "name",nullable = false)
	@ApiModelProperty("角色名称")
	private String name;
	
    // 描述
    @Column(name = "description",nullable = false)
	@ApiModelProperty("描述")
	private String description;
	
    // 用户ID列表，逗号分隔
    @Column(name = "profileids")
	@ApiModelProperty("用户ID列表，逗号分隔")
	private String profileids;
	

    public void copy(Roles source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}