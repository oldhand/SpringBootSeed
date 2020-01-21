package com.github.cores.repository;

import com.github.cores.domain.Saass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface SaassRepository extends JpaRepository<Saass, Long>, JpaSpecificationExecutor<Saass> {

    Saass findByName(String name);
}