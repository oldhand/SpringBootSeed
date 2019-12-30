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
    private Long id;
    private Timestamp published;
    private Timestamp updated;
    private String author;
    private Integer deleted;
    private Integer createnew;
}
