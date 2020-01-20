package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Parenttabs;
import com.github.cores.service.dto.ParenttabsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-14
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParenttabsMapper extends BaseMapper<ParenttabsDTO, Parenttabs> {

}