package com.github.samples.service.dto;

import lombok.Data;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2019-12-23
*/
@Data
public class UserQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String username;
}