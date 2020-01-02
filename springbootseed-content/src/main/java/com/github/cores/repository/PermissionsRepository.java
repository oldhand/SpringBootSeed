package com.github.cores.repository;

import com.github.cores.domain.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface PermissionsRepository extends JpaRepository<Permissions, Long>, JpaSpecificationExecutor<Permissions> {
}