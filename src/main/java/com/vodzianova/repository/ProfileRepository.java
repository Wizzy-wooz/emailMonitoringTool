package com.vodzianova.repository;

import com.vodzianova.domain.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    List<Profile> findAll();

}
