package com.github.repository;

import com.github.domain.GenConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author oldhand
 * @date 2019-12-16
*/
public interface GenConfigRepository extends JpaRepository<GenConfig,Long> {
}
