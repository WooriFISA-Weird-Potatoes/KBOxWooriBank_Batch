package com.woorifisa.kboxwoori.batch.scheduler;

import com.woorifisa.kboxwoori.batch.prediction.PredictionJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PredictionScheduler {

    private final JobLauncher jobLauncher;
    private final PredictionJobConfig predictionJobConfig;

    @Scheduled(cron = "0 0 1 * * *")
    public void run() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDate.now().toString())
                .addString("dateTime", LocalDateTime.now().toString())
                .addLong("random", System.currentTimeMillis())
                .toJobParameters();

        try {
            JobExecution jobExecution = jobLauncher.run(predictionJobConfig.predictionJob(), jobParameters);

            SchedulerLog.log(jobExecution);

        } catch (JobInstanceAlreadyCompleteException
                | JobExecutionAlreadyRunningException
                | JobParametersInvalidException
                | JobRestartException e) {
            e.printStackTrace();
        }
    }

}
