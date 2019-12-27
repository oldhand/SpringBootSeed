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
}