package com.github.cores.repository;

import com.github.cores.domain.Modentitynos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface ModentitynosRepository extends JpaRepository<Modentitynos, Long>, JpaSpecificationExecutor<Modentitynos> {
    Modentitynos findByTabid(int tabid);
}