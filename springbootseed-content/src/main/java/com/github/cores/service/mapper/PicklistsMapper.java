package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Picklists;
import com.github.cores.service.dto.PicklistsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-03
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PicklistsMapper extends BaseMapper<PicklistsDTO, Picklists> {

}