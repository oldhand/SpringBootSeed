package com.github.localstorage.repository;

import com.github.localstorage.domain.LocalStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author oldhand
* @date 2019-12-16
*/
public interface LocalStorageRepository extends JpaRepository<LocalStorage, Long>, JpaSpecificationExecutor<LocalStorage> {

    LocalStorage findByMd5(String md5);

}