package com.woorifisa.kboxwoori.batch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;

@Slf4j
public class SchedulerLog {

    public static void log(JobExecution jobExecution) {
        log.info("Job Execution: " + jobExecution.getStatus());
        log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
        log.info("Job getJobId: " + jobExecution.getJobId());
        log.info("Job getExitStatus: " + jobExecution.getExitStatus());
        log.info("Job getJobInstance: " + jobExecution.getJobInstance());
        log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
        log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
        log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());
    }
}
