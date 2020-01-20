package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-02
*/
@Data
public class DeptsDTO extends BaseDTO implements Serializable {
	
    // 部门名称
    private String name;
	
    // 部门ID
    private String deptid;
	
    // 父部门ID
    private String parentid;
	
    // 排序号
    private Integer sequence;
	
    // 部门级别
    private Integer depth;
	
    // saasid
    private Long saasid;
	
}