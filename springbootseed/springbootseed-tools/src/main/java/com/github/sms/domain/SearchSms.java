package com.github.sms.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Getter
@Setter
@ApiModel
public class SearchSms {

    // 手机号码
    @ApiModelProperty("手机号码")
    private String mobile;

    // 国际区号
    @ApiModelProperty("国际区号")
    private String regioncode;

    @Override
    public String toString() {
        return "{mobile=" + mobile  + ", regioncode=" + regioncode  + "}";
    }
}
