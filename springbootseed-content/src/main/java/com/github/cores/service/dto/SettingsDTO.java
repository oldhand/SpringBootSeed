package com.github.cores.service.dto;

import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-15
*/
@Data
public class SettingsDTO implements Serializable {

    private Long id;
    // 系统名称
    private String name;
	
    // 域名
    private String site;
	
    // 网站备案号
    private String icp;
	
    // Logo
    private String logo;
	
    // 版权公司
    private String companyname;
	
    // 是否单点登录
    private Boolean singlelogin;
	
}