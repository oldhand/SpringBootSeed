package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Settings;
import com.github.cores.service.dto.SettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-15
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SettingsMapper extends BaseMapper<SettingsDTO, Settings> {

}