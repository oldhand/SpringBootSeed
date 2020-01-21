package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-15
*/
@Data
public class ModentitynosDTO extends BaseDTO implements Serializable {
	
    // 云服务ID
    private Long saasid;
	
    // 模块ID
    private Integer tabid;
	
    // 编号前缀
    private String prefix;
	
    // 当前编号
    private Integer curId;
	
    // 起始编号
    private Integer startId;
	
    // 是否激活
    private Boolean active;
	
    // 编号长度
    private Integer length;
	
    // 是否包含日期
    private Boolean includeDate;

    // 当前日期
    private String curDate;
	
}