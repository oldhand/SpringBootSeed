package com.github.rabbitmq.service.mapper;

import com.github.base.BaseMapper;
import com.github.rabbitmq.domain.Mq;
import com.github.rabbitmq.service.dto.MqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author oldhand
* @date 2019-12-25
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MqMapper extends BaseMapper<MqDTO, Mq> {

}