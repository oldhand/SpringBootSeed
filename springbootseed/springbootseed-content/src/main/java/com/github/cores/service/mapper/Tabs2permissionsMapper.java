package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Tabs2permissions;
import com.github.cores.service.dto.Tabs2permissionsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-14
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Tabs2permissionsMapper extends BaseMapper<Tabs2permissionsDTO, Tabs2permissions> {

}