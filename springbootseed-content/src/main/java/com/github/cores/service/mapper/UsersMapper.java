package com.github.cores.service.mapper;

import com.github.base.BaseMapper;
import com.github.cores.domain.Users;
import com.github.cores.service.dto.UsersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-02
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMapper extends BaseMapper<UsersDTO, Users> {

}