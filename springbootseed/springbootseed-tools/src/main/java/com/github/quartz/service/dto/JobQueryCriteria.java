package com.github.quartz.service.dto;

import lombok.Data;
import com.github.annotation.Query;

import java.sql.Timestamp;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Data
public class JobQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String jobName;

    @Query
    private Boolean isSuccess;

    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    private Timestamp starttime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    private Timestamp endtime;
}
