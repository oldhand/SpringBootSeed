package com.github.repository;

import com.github.domain.ContentIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-01
*/
public interface ContentIdsRepository extends JpaRepository<ContentIds, Long>, JpaSpecificationExecutor<ContentIds> {
}