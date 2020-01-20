package com.github.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @JsonSerialize(using= ToStringSerializer.class)
    protected Long id;
    protected Timestamp published;
    protected Timestamp updated;
    protected String author;
    protected Integer deleted;
}
