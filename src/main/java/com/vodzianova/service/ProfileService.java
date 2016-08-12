package com.vodzianova.service;

import com.vodzianova.domain.Profile;

import java.util.List;

/**
 * @author Elena Vodzianova 11/08/2016
 */
public interface ProfileService {

    void save(Profile profile);

    List<Profile> findAll();
}
