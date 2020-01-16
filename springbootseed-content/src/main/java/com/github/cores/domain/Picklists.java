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
@Table(name="base_picklists")
public class Picklists extends BaseEntity implements Serializable {
	
    // saasid
    @Column(name = "saasid",nullable = false)
	@ApiModelProperty("saasid")
	private Long saasid;
	
    // 名称
    @Column(name = "name",nullable = false)
	@ApiModelProperty("名称")
	private String name;
	
    // 可见
    @Column(name = "presence",nullable = false)
	@ApiModelProperty("可见")
	private Boolean presence;
	
    // 排序号
    @Column(name = "sequence",nullable = false)
	@ApiModelProperty("排序号")
	private Integer sequence;
	
    // 存储值
    @Column(name = "value",nullable = false)
	@ApiModelProperty("存储值")
	private String value;
	
    // 显示值
    @Column(name = "display",nullable = false)
	@ApiModelProperty("显示值")
	private String display;
	

    public void copy(Picklists source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}