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
@Table(name="base_parenttabs")
public class Parenttabs extends BaseEntity implements Serializable {
	
    @Column(name = "saasid",nullable = false)
	private Long saasid;
	
    // 父模块名称
    @Column(name = "tabname",nullable = false)
	@ApiModelProperty("父模块名称")
	private String tabname;
	
    // 父模块标签
    @Column(name = "tablabel",nullable = false)
	@ApiModelProperty("父模块标签")
	private String tablabel;
	
    // 是否可见
    @Column(name = "presence",nullable = false)
	@ApiModelProperty("是否可见")
	private Integer presence;
	
    // 排序号
    @Column(name = "squence",nullable = false)
	@ApiModelProperty("排序号")
	private Integer squence;
	
    // 图标
    @Column(name = "icon",nullable = false)
	@ApiModelProperty("图标")
	private String icon;
	

    public void copy(Parenttabs source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}