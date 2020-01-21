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
* @date 2020-01-15
*/
@Entity
@Data
@Table(name="base_modentitynos")
public class Modentitynos extends BaseEntity implements Serializable {
	
    // 云服务ID
    @Column(name = "saasid",nullable = false)
	@ApiModelProperty("云服务ID")
	private Long saasid;
	
    // 模块ID
    @Column(name = "tabid",nullable = false)
	@ApiModelProperty("模块ID")
	private Integer tabid;
	
    // 编号前缀
    @Column(name = "prefix",nullable = false)
	@ApiModelProperty("编号前缀")
	private String prefix;
	
    // 当前编号
    @Column(name = "cur_id",nullable = false)
	@ApiModelProperty("当前编号")
	private Integer curId;
	
    // 起始编号
    @Column(name = "start_id",nullable = false)
	@ApiModelProperty("起始编号")
	private Integer startId;
	
    // 是否激活
    @Column(name = "active",nullable = false)
	@ApiModelProperty("是否激活")
	private Boolean active;
	
    // 编号长度
    @Column(name = "length",nullable = false)
	@ApiModelProperty("编号长度")
	private Integer length;
	
    // 是否包含日期
    @Column(name = "include_date",nullable = false)
	@ApiModelProperty("是否包含日期")
	private Boolean includeDate;

    // 当前日期
    @Column(name = "cur_date",nullable = false)
    @ApiModelProperty("当前日期")
    private String curDate;

    public void copy(Modentitynos source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}