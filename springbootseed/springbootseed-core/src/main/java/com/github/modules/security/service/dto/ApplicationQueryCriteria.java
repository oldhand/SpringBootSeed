package com.github.modules.security.service.dto;

import com.github.annotation.Query;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Data
public class ApplicationQueryCriteria implements Serializable {

    @Query
    private String appid;

    @Query
    private Long id;

    // 多字段模糊
    @Query(blurry = "appid,secret")
    private String blurry;


}
