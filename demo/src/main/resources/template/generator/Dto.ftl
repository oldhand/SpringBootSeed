package ${package}.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if !auto && pkColumnType = 'Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>


/**
* @author ${author}
* @date ${date}
*/
@Data
public class ${className}DTO extends BaseDTO implements Serializable {
<#if columns??>
    <#list columns as column>
	<#if column.columnName != 'id'>
    <#if column.columnComment != ''>
    // ${column.columnComment}
    </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    // 处理精度丢失问题
    @JsonSerialize(using= ToStringSerializer.class)
    </#if>
    </#if>
    private ${column.columnType} ${column.changeColumnName};
	</#if>
	
    </#list>
</#if>
}