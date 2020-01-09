package com.github.cores.repository;

import com.github.cores.domain.Picklists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-03
*/
public interface PicklistsRepository extends JpaRepository<Picklists, Long>, JpaSpecificationExecutor<Picklists> {
}