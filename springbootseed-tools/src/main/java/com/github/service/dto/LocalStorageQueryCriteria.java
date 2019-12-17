package com.github.service.dto;

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
    @Query(blurry = "name,suffix,type,operate,size")
    private String blurry;

    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    private Timestamp startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    private Timestamp endTime;
}