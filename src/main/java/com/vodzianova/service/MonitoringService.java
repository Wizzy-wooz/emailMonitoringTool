package com.vodzianova.service;

import com.vodzianova.domain.Profile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Service
public class MonitoringService {

    //    TODO: test coverage with Mockito
    private static Logger logger = getLogger(MonitoringService.class);

    @Autowired
    private EmailReceiverService emailReceiverService;

    @Autowired
    private ProfileService profileService;

    public MonitoringService() {
    }

    public void startMonitoring() {
        logger.info("Email monitoring started...");
        List<Profile> profiles = profileService.findAll();
        for (Profile profile : profiles) {
            emailReceiverService.downloadEmails(profile, findProtocolByPortNumber(profile.getPort()),
                    findHostByPortNumber(profile.getPort()), profile.getPort(), profile.getEmail(), profile.getPassword(),
                    profile.getDirectory());
        }

    }

    //    TODO: make normal map!
    private String findHostByPortNumber(String port) {
        if (port.equals("993")) {
            return "imap.gmail.com";
        } else if (port.equals("995")) {
            return "pop.gmail.com";
        } else {
            return null;
        }

    }

    //    TODO: make normal map!
    private String findProtocolByPortNumber(String port) {
        if (port.equals("993")) {
            return "imap";
        } else if (port.equals("995")) {
            return "pop3";
        } else {
            return null;
        }

    }
}
