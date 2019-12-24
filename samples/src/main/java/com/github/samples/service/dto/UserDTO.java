package com.github.samples.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;


/**
* @author oldhand
* @date 2019-12-23
*/
@Data
public class UserDTO implements Serializable {

    private Long id;

    // 创建时间
    private Timestamp createTime;

    private String email;

    private Boolean enabled;

    private Timestamp lastPasswordResetTime;

    private String password;

    private String phone;

    // 用户名
    private String username;

    private Long deptId;

    private Long jobId;

    private Long avatarId;
}