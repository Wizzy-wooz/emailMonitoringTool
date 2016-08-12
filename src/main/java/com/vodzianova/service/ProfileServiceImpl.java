package com.vodzianova.service;

import com.vodzianova.domain.Profile;
import com.vodzianova.repository.ProfileRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    //    TODO: test coverage with Mockito
    private static Logger logger = getLogger(ProfileServiceImpl.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public void save(Profile profile) {
        logger.info("Start saving profile to database...");
        profileRepository.save(profile);
    }

    @Override
    public List<Profile> findAll() {
        logger.info("Start fetching all profiles from database...");
        return profileRepository.findAll();
    }
}
