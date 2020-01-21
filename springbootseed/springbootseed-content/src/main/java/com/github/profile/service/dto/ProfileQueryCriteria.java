package com.github.profile.service.dto;

import lombok.Data;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2019-12-27
*/
@Data
public class ProfileQueryCriteria{

    // 精确
    @Query
    private String id;

    // 精确
    @Query
    private String username;

    // 用户类型
    @Query
    private String type;

    // 国家代码
    @Query
    private String regioncode;

    // 手机号码
    @Query
    private String mobile;
}