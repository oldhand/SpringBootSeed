package com.github.rabbitmq.repository;

import com.github.rabbitmq.domain.Mq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2019-12-25
*/
public interface MqRepository extends JpaRepository<Mq, Long>, JpaSpecificationExecutor<Mq> {

    Mq findByUniquekey(String key);
}