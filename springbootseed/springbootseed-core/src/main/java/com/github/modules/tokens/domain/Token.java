package com.github.modules.tokens.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel
public class Token {

    // 令牌
    @ApiModelProperty("令牌")
    private String token;

    // 用户ID
    @ApiModelProperty("用户ID")
    @JsonIgnore
    private String profileid;

    // 有效时间
    @ApiModelProperty("有效时间")
    private int expire;

    @Override
    public String toString() {
        return "{token=" + token  + ",profileid=" + profileid  + ",expire=" + expire  + "}";
    }
}
