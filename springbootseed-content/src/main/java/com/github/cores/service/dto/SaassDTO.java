package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-03
*/
@Data
public class SaassDTO extends BaseDTO implements Serializable {
	
    // 名称
    private String name;
	
    // 公司名称
    private String companyname;
	
    // 短名称
    private String shortname;
	
    // 省份
    private String province;
	
    // 城市
    private String city;
	
    // 创建人
    private String profileid;
	
    // 联系人
    private String contact;
	
    // 联系电话
    private String mobile;
	
    // 审批状态
    private Integer approvalstatus;
	
    // 审批人
    private String approver;
	
}