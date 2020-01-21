package com.github.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author oldhand
* @date 2019-12-27
*/
@Entity
@Data
@Table(name="content_ids")
public class ContentIds implements Serializable {

    //记录ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",insertable=false, updatable=false)
    @ApiModelProperty("记录ID")
	private Long id;

    // 存储表名
    @Column(name = "xn_type",updatable=false,nullable = false)
	@ApiModelProperty("存储表名")
	private String xnType;

    public void copy(ContentIds source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}