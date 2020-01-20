package com.github.cores.repository;

import com.github.cores.domain.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface SettingsRepository extends JpaRepository<Settings, Long>, JpaSpecificationExecutor<Settings> {
}