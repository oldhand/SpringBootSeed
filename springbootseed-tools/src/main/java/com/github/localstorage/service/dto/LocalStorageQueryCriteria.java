package com.github.localstorage.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2019-12-16
*/
@Data
public class LocalStorageQueryCriteria{

    // 模糊
    @Query(blurry = "name,sourceName,suffix,type,operate,size,md5")
    private String blurry;

    @Query(type = Query.Type.GREATER_THAN,propName = "published")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "published")
    private Timestamp endTime;
}