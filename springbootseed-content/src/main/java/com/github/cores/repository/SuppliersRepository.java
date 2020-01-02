package com.github.cores.repository;

import com.github.cores.domain.Suppliers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface SuppliersRepository extends JpaRepository<Suppliers, Long>, JpaSpecificationExecutor<Suppliers> {
}