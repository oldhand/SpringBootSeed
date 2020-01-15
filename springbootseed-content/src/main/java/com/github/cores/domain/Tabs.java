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
@Table(name="base_tabs")
public class Tabs extends BaseEntity implements Serializable {
	
    @Column(name = "saasid",nullable = false)
	private Long saasid;
	
    // 模块名称
    @Column(name = "tabname",nullable = false)
	@ApiModelProperty("模块名称")
	private String tabname;

    // 父模块名称
    @Column(name = "parenttab",nullable = false)
    @ApiModelProperty("父模块名称")
    private String parenttab;
	
    // 模块标签
    @Column(name = "tablabel",nullable = false)
	@ApiModelProperty("模块标签")
	private String tablabel;
	
    // 排序号
    @Column(name = "sequence",nullable = false)
	@ApiModelProperty("排序号")
	private Integer sequence;
	
    // 模块ID
    @Column(name = "tabid",nullable = false)
	@ApiModelProperty("模块ID")
	private Integer tabid;
	
    // 图标
    @Column(name = "icon",nullable = false)
	@ApiModelProperty("图标")
	private String icon;
	
    // 存储类型
    @Column(name = "datatype",nullable = false)
	@ApiModelProperty("存储类型")
	private Integer datatype;
	
    // 是否可见
    @Column(name = "presence",nullable = false)
	@ApiModelProperty("是否可见")
	private Integer presence;
	

    public void copy(Tabs source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}