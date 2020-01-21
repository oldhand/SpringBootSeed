package com.github.rabbitmq.service.dto;

import lombok.Data;
import com.github.annotation.Query;

/**
* @author oldhand
* @date 2019-12-25
*/
@Data
public class MqQueryCriteria{

    // 精确
    @Query
    private Long id;

    // 精确
    @Query
    private String uniquekey;
}