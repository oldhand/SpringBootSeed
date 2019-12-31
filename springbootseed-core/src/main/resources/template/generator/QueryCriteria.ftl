package ${package}.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.sql.Timestamp;
<#if queryHasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if queryColumns??>
import com.github.annotation.Query;
</#if>

/**
* @author ${author}
* @date ${date}
*/
@Data
public class ${className}QueryCriteria{
<#if queryColumns??>
	 
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
    <#list queryColumns as column>

    <#if column.columnQuery = '1'>
    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    </#if>
    <#if column.columnQuery = '2'>
    // 精确
    @Query
    </#if>
    <#if column.columnComment != ''>
	@ApiModelProperty("${column.columnComment}")
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
}