package com.github.profile.service.mapper;

import com.github.base.BaseMapper;
import com.github.profile.domain.Profile;
import com.github.profile.service.dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2019-12-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper extends BaseMapper<ProfileDTO, Profile> {

}