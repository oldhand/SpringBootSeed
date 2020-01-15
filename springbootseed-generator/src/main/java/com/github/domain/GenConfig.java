package com.github.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 代码生成配置
 * @author oldhand
 * @date 2019-12-16
*/
@Data
@Entity
@Table(name = "gen_config")
public class GenConfig {

    @Id
    @ApiModelProperty("包ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    // 包路径
    @Column(name = "package_name")
    @ApiModelProperty("包名")
    private String packageName;

    // 模块名
    @Column(name = "module_name")
    @ApiModelProperty("模块名")
    private String moduleName;

    // 作者
    @ApiModelProperty("作者")
    private String author;

    // 表前缀
    @ApiModelProperty("表前缀")
    private String prefix;

    // 是否覆盖
    @ApiModelProperty("是否覆盖")
    private Boolean cover;
}