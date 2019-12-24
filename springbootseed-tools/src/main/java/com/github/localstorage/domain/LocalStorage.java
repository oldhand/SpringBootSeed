package com.github.localstorage.domain;

import lombok.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
* @author oldhand
* @date 2019-12-16
*/
@Getter
@Setter
@Entity
@ToString
@ApiModel
@Table(name="local_storage")
@NoArgsConstructor
public class LocalStorage  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty("文件ID")
    private Long id;

    // 真实文件名,相对路径
    @Column(name = "real_name")
    @ApiModelProperty("真实文件名,相对路径")
    private String realName;

    // 文件名
    @Column(name = "name")
    @ApiModelProperty("文件名")
    private String name;

    // 原上传文件名
    @Column(name = "source_name")
    @ApiModelProperty("原上传文件名")
    private String sourceName;

    // 后缀
    @Column(name = "suffix")
    @ApiModelProperty("后缀")
    private String suffix;

    // 路径
    @Column(name = "path")
    @ApiModelProperty("相对路径")
    private String path;

    // 类型
    @Column(name = "type")
    @ApiModelProperty("文件类型")
    private String type;

    @Column(name = "md5",unique = true)
    @ApiModelProperty("文件MD5")
    private String md5;

    // 大小
    @Column(name = "size")
    @ApiModelProperty("文件大小")
    private String size;

    // 操作人
    @Column(name = "operate")
    @ApiModelProperty("操作人")
    private String operate;

    @Column(name = "published")
    @ApiModelProperty("创建日期")
    @CreationTimestamp
    private Timestamp published;

    public LocalStorage(String realName,String name, String sourceName, String suffix, String path, String type,String md5, String size, String operate) {
        this.realName = realName;
        this.name = name;
        this.sourceName = sourceName;
        this.suffix = suffix;
        this.path = path;
        this.type = type;
        this.md5 = md5;
        this.size = size;
        this.operate = operate;
    }

    public void copy(LocalStorage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
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