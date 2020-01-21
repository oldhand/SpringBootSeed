package com.github.service.mapper;

import com.github.base.BaseMapper;
import com.github.domain.Log;
import com.github.service.dto.LogSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogSmallMapper extends BaseMapper<LogSmallDTO, Log> {

}