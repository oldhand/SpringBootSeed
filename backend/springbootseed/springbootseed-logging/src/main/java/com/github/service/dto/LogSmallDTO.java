package com.github.service.dto;

import lombok.Data;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Data
public class LogSmallDTO implements Serializable {

    // 描述
    private String description;

    // 请求ip
    private String requestIp;

    // 请求耗时
    private Long time;

    private String address;

    private String browser;

    // 创建日期
    private Timestamp createTime;
}
