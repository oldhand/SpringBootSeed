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

//
///**
// * @author oldhand
// * @date 2019-12-16
// */
//@Getter
//@Setter
//@ToString
//@ApiModel
//public class AuthApplication {
//
//    @NotBlank
//    @ApiModelProperty("应用ID")
//    private String appid;
//
//    @NotBlank
//    @ApiModelProperty("密钥，32位字符串")
//    private String secret;
//
//    @ApiModelProperty("格林威治(GMT)时间戳，北京时间减8小时")
//    private long timestamp;
//
//
//    @Override
//    public String toString() {
//        return "{appid=" + appid  + ", secret= ******}";
//    }
//}
