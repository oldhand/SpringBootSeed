package com.github.sms.service.dto;

import com.github.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
* @author oldhand
* @date 2020-01-21
*/
@Data
public class SmslogQueryCriteria{
	 
	// 精确
	
    @Query(type = Query.Type.GREATER_THAN,propName = "published")
	@ApiModelProperty("起始时间")
    private Timestamp starttime;

    @Query(type = Query.Type.LESS_THAN,propName = "published")
	@ApiModelProperty("结束时间")
    private Timestamp endtime;

    @Query
    @ApiModelProperty("创建者")
    private String profileid;

    @Query
    @ApiModelProperty("云服务ID")
    private Long saasid;

    @Query
    @ApiModelProperty("手机号码")
    private String mobile;

    // 国际区号
    @Query
    @ApiModelProperty("国际区号")
    private String regioncode;

    @Query
    @ApiModelProperty("状态")
    private Integer status;


}