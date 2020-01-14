package com.github.cores.repository;

import com.github.cores.domain.Tabs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface TabsRepository extends JpaRepository<Tabs, Long>, JpaSpecificationExecutor<Tabs> {
}