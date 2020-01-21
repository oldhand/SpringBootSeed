package com.github.localstorage.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author oldhand
* @date 2019-12-16
*/
@Getter
@Setter
public class LocalStorageDTO  implements Serializable {

    // ID
    private Long id;

    private String realName;

    private String sourceName;

    // 文件名
    private String name;

    private String path;

    // 后缀
    private String suffix;

    // 类型
    private String type;

    private String md5;

    // 大小
    private String size;

    // 操作人
    private String operate;

    private Timestamp published;
}