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
public class ParenttabsDTO extends BaseDTO implements Serializable {
	
    private Long saasid;
	
    // 父模块名称
    private String tabname;
	
    // 父模块标签
    private String tablabel;
	
    // 是否可见
    private Boolean presence;
	
    // 排序号
    private Integer squence;
	
    // 图标
    private String icon;
	
}