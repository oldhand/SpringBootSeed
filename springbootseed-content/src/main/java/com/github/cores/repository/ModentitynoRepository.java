package com.github.cores.repository;

import com.github.cores.domain.Modentityno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface ModentitynoRepository extends JpaRepository<Modentityno, Long>, JpaSpecificationExecutor<Modentityno> {
}