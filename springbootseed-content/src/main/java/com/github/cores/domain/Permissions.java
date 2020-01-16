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
@Table(name="base_permissions")
public class Permissions extends BaseEntity implements Serializable {

    // saasid
    @Column(name = "saasid",nullable = false)
    @ApiModelProperty("saasid")
    private Long saasid;

    @Column(name = "name",nullable = false)
	private String name;
	
    @Column(name = "description",nullable = false)
	private String description;
	
    @Column(name = "allowdeleted",nullable = false)
	private Boolean allowdeleted;
	
    @Column(name = "global_all_view",nullable = false)
	private Boolean globalAllView;
	
    @Column(name = "global_all_edit",nullable = false)
	private Boolean globalAllEdit;
	

    public void copy(Permissions source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}