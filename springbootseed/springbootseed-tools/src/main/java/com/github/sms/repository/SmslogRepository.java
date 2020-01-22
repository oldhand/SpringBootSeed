package com.github.sms.repository;

import com.github.sms.domain.Smslog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-21
*/
public interface SmslogRepository extends JpaRepository<Smslog, Long>, JpaSpecificationExecutor<Smslog> {
}