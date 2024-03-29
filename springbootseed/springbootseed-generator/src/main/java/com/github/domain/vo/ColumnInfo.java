package com.github.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 列的数据信息
 * @author oldhand
 * @date 2019-12-16
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {

    // 数据库字段名称
	@ApiModelProperty("字段名称")
    private Object columnName;

    // 允许空值
	@ApiModelProperty("允许空值")
    private Object isNullable;

    // 数据库字段类型
	@ApiModelProperty("字段类型")
    private Object columnType;

    // 数据库字段注释
	@ApiModelProperty("字段注释")
    private Object columnComment;

    // 数据库字段键类型
	@ApiModelProperty("字段键类型")
    private Object columnKey;

    // 额外的参数
	@ApiModelProperty("额外的参数")
    private Object extra;

    // 查询 1:模糊 2：精确
	@ApiModelProperty("查询 1:模糊 2：精确")
    private String columnQuery;

    // 是否在列表显示
	@ApiModelProperty("是否在列表显示")
    private String columnShow;
}
