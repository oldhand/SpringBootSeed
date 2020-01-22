package com.github.sms.service.mapper;

import com.github.base.BaseMapper;
import com.github.sms.domain.Smslog;
import com.github.sms.service.dto.SmslogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2020-01-21
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SmslogMapper extends BaseMapper<SmslogDTO, Smslog> {

}