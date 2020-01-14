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
public class Tabs2permissionsDTO extends BaseDTO implements Serializable {
	
    private Long saasid;
	
    // 权限ID
    private Long permissionid;
	
    // 模块ID
    private Integer tabid;
	
    // 全部权限
    private Integer all;
	
    // 编辑权限
    private Integer edit;
	
    // 删除权限
    private Integer delete;
	
    // 查询权限
    private Integer query;
	
    // 新增权限
    private Integer add;
	
}