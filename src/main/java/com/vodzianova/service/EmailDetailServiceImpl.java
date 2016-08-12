package com.vodzianova.service;

import com.vodzianova.domain.EmailDetail;
import com.vodzianova.repository.EmailDetailRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author Elena Vodzianova
 */
@Service
public class EmailDetailServiceImpl implements EmailDetailService {

    //    TODO: test coverage with Mockito
    private static Logger logger = getLogger(EmailDetailServiceImpl.class);

    @Autowired
    private EmailDetailRepository emailDetailRepository;

    @Override
    public EmailDetail save(EmailDetail emailDetail) {
        logger.debug("Email detail object " + emailDetail.getId() + "is being saved.");
        return emailDetailRepository.save(emailDetail);
    }

    @Override
    public List<EmailDetail> findAll() {
        logger.debug("Email details are fetching from database...");
        return emailDetailRepository.findAll();
    }
}
