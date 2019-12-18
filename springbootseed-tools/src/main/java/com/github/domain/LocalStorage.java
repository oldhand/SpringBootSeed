package com.github.domain;

import lombok.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.github.base.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author oldhand
* @date 2019-12-16
*/
@Getter
@Setter
@Entity
@Table(name="local_storage")
@NoArgsConstructor
public class LocalStorage  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 真实文件名,相对路径
    @Column(name = "real_name")
    private String realName;

    // 文件名
    @Column(name = "name")
    private String name;

    // 原上传文件名
    @Column(name = "source_name")
    private String sourceName;

    // 后缀
    @Column(name = "suffix")
    private String suffix;

    // 路径
    @Column(name = "path")
    private String path;

    // 类型
    @Column(name = "type")
    private String type;

    @Column(name = "md5",unique = true)
    private String md5;

    // 大小
    @Column(name = "size")
    private String size;

    // 操作人
    @Column(name = "operate")
    private String operate;

    @Column(name = "published")
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