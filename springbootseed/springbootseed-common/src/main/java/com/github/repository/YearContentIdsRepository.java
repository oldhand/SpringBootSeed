package com.github.repository;

import com.github.domain.YearContentIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-01
*/
public interface YearContentIdsRepository extends JpaRepository<YearContentIds, Long>, JpaSpecificationExecutor<YearContentIds> {
}