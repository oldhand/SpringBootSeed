package com.github.cores.repository;

import com.github.cores.domain.Tabs2permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface Tabs2permissionsRepository extends JpaRepository<Tabs2permissions, Long>, JpaSpecificationExecutor<Tabs2permissions> {
}