package com.github.cores.service.dto;

import com.github.base.BaseDTO;
import lombok.Data;
import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


/**
* @author oldhand
* @date 2020-01-02
*/
@Data
public class RolesDTO extends BaseDTO implements Serializable {
	
    private String name;
	
    private String description;
	
    private String profileids;
	
}