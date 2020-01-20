package com.github.profile.repository;

import com.github.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
* @author oldhand
* @date 2019-12-27
*/
public interface ProfileRepository extends JpaRepository<Profile, String>, JpaSpecificationExecutor<Profile> {

    Profile findByUsername(String username);

    @Query("select p from Profile p where p.id = ?1")
    Profile myfindById(String id);


}