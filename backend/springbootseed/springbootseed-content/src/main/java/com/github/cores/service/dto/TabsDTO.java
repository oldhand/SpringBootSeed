package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-14
*/
@Data
public class TabsDTO extends BaseDTO implements Serializable {
	
    private Long saasid;
	
    // 模块名称
    private String tabname;

    // 父模块名称
    private String parenttab;

    // 模块标签
    private String tablabel;
	
    // 排序号
    private Integer sequence;
	
    // 模块ID
    private Integer tabid;
	
    // 图标
    private String icon;
	
    // 存储类型
    private Integer datatype;
	
    // 是否可见
    private Boolean presence;
	
}