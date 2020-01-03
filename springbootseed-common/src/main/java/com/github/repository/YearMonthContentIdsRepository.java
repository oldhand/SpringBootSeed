package com.github.repository;

import com.github.domain.YearMonthContentIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-01
*/
public interface YearMonthContentIdsRepository extends JpaRepository<YearMonthContentIds, Long>, JpaSpecificationExecutor<YearMonthContentIds> {
}