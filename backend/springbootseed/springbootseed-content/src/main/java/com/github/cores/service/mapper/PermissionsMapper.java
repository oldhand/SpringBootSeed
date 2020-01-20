package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Permissions;
import com.github.cores.service.dto.PermissionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-02
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionsMapper extends BaseMapper<PermissionsDTO, Permissions> {

}