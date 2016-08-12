package com.vodzianova.service;

import com.vodzianova.domain.EmailDetail;

import java.util.List;

/**
 * @author Elena Vodzianova 11/08/2016
 */
public interface EmailDetailService {

    EmailDetail save(EmailDetail emailDetail);

    List<EmailDetail> findAll();
}
