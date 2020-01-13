package com.github.modules.security.service.mapper;

import com.github.base.BaseMapper;
import com.github.modules.security.domain.Application;
import com.github.modules.security.service.dto.ApplicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author oldhand
 * @date 2019-12-16
*/

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationMapper extends BaseMapper<ApplicationDTO, Application> {
}

