package com.github.samples.service.dto;

import lombok.Data;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2019-12-19
*/
@Data
public class UserQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String email;

    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String phone;

    // 精确
    @Query
    private String username;
}