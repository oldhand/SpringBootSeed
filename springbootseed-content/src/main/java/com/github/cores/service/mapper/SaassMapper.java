package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Saass;
import com.github.cores.service.dto.SaassDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-14
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SaassMapper extends BaseMapper<SaassDTO, Saass> {

}