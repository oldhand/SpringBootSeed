package com.github.base;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author oldhand
 * @Date 2019-12-16
 */
@Getter
@Setter
public class BaseDTO  implements Serializable {

    private Boolean isDelete;

    private Timestamp createTime;

    private Timestamp updateTime;
}
