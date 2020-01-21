package com.github.modules.security.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Getter
@Setter
public class ApplicationDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    private String appid;

    private String secret;

    private String description;

    private Timestamp published;
}
