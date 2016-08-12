package com.vodzianova.repository;

import com.vodzianova.domain.EmailDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Elena Vodzianova 11/08/2016
 */

@Repository
public interface EmailDetailRepository extends JpaRepository<EmailDetail, Long> {

}




