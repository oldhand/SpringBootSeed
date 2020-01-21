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
public class RolesDTO extends BaseDTO implements Serializable {
	
    private Long saasid;
	
    // 角色名称
    private String name;
	
    // 描述
    private String description;
	
    // 用户ID列表，逗号分隔
    private String profileids;
	
}