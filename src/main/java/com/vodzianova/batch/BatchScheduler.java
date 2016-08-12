package com.vodzianova.batch;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Configuration
@EnableScheduling
class BatchScheduler {

    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(
            JpaTransactionManager txManager) throws Exception {

        MapJobRepositoryFactoryBean factory = new
                MapJobRepositoryFactoryBean(txManager);
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");

        factory.afterPropertiesSet();

        return factory;
    }

    @Bean
    public JobRepository jobRepository(
            MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

}
