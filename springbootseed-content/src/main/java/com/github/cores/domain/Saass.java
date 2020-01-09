package com.github.cores.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.base.BaseEntity;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;

/**
* @author oldhand
* @date 2020-01-03
*/
@Entity
@Data
@Table(name="base_saass")
public class Saass extends BaseEntity implements Serializable {
	
    // 名称
    @Column(name = "name")
	@ApiModelProperty("名称")
	private String name;
	
    // 公司名称
    @Column(name = "companyname")
	@ApiModelProperty("公司名称")
	private String companyname;
	
    // 短名称
    @Column(name = "shortname")
	@ApiModelProperty("短名称")
	private String shortname;
	
    // 省份
    @Column(name = "province")
	@ApiModelProperty("省份")
	private String province;
	
    // 城市
    @Column(name = "city")
	@ApiModelProperty("城市")
	private String city;
	
    // 创建人
    @Column(name = "profileid")
	@ApiModelProperty("创建人")
	private String profileid;
	
    // 联系人
    @Column(name = "contact")
	@ApiModelProperty("联系人")
	private String contact;
	
    // 联系电话
    @Column(name = "mobile")
	@ApiModelProperty("联系电话")
	private String mobile;
	
    // 审批状态
    @Column(name = "approvalstatus")
	@ApiModelProperty("审批状态")
	private Integer approvalstatus;
	
    // 审批人
    @Column(name = "approver")
	@ApiModelProperty("审批人")
	private String approver;
	

    public void copy(Saass source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}