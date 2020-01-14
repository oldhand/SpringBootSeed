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
public class PicklistsDTO extends BaseDTO implements Serializable {
	
    // saasid
    private Long saasid;
	
    // 名称
    private String name;
	
    // 可见
    private Integer presence;
	
    // 排序号
    private Integer sequence;
	
    // 存储值
    private String value;
	
    // 显示值
    private String display;
	
}