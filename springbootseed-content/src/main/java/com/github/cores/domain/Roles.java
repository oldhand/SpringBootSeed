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
@Table(name="base_roles")
public class Roles extends BaseEntity implements Serializable {
	
    @Column(name = "name",nullable = false)
	private String name;
	
    @Column(name = "description")
	private String description;
	
    @Column(name = "profileids",nullable = false)
	private String profileids;
	

    public void copy(Roles source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}