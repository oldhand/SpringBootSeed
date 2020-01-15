package com.github.cores.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Timestamp;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2020-01-14
*/
@Data
public class RolesQueryCriteria{
	 
	// 精确
	@Query
    @ApiModelProperty("删除标记(0表示正常数据，1表示逻辑删除)")
    private Integer deleted;	
	
    @Query(type = Query.Type.GREATER_THAN,propName = "published")
	@ApiModelProperty("起始时间")
    private Timestamp starttime;

    @Query(type = Query.Type.LESS_THAN,propName = "published")
	@ApiModelProperty("结束时间")
    private Timestamp endtime; 

    // 精确
    @Query
	@ApiModelProperty("ID")
    private Long id;

    @Query
    @ApiModelProperty("创建者")
    private String author;

    // 精确
    @Query
    @ApiModelProperty("云服务ID")
    private Long saasid;
}