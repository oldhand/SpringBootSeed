package com.github.cores.repository;

import com.github.cores.domain.Parenttabs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface ParenttabsRepository extends JpaRepository<Parenttabs, Long>, JpaSpecificationExecutor<Parenttabs> {
}