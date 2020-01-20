package com.github.localstorage.service.mapper;

import com.github.base.BaseMapper;
import com.github.localstorage.domain.LocalStorage;
import com.github.localstorage.service.dto.LocalStorageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2019-12-16
*/
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapper extends BaseMapper<LocalStorageDTO, LocalStorage> {

}