package com.vodzianova.batch;

import com.vodzianova.service.MonitoringService;
import org.apache.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

import static org.apache.log4j.Logger.getLogger;


/**
 * @author Elena Vodzianova 11/08/2016
 */
@Configuration
@EnableBatchProcessing
@Import({BatchScheduler.class})
class BatchConfiguration {

    private static Logger logger = getLogger(BatchConfiguration.class);

    @Autowired
    private MonitoringService monitoringService;

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Scheduled(cron = "${email.monitor.cron}")
    public void perform() throws Exception {

        logger.debug("Job Started at :" + new Date());

        JobParameters param = new JobParametersBuilder().addString("JobID",
                String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution execution = jobLauncher.run(job(), param);

        logger.debug("Job finished with status :" + execution.getStatus());
    }

    @Bean
    protected Tasklet tasklet() {
        return (contribution, context) -> {
            logger.debug("Email monitoring started...");
            monitoringService.startMonitoring();
            logger.debug("Email monitoring is completed.");
            return RepeatStatus.FINISHED;
        };

    }

    @Bean
    public Job job() throws Exception {
        return this.jobs.get("job").start(step1()).build();
    }

    @Bean
    protected Step step1() throws Exception {
        return this.steps.get("step1").tasklet(tasklet()).build();
    }

}
