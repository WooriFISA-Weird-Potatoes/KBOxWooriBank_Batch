package com.woorifisa.kboxwoori.batch.event;

import com.woorifisa.kboxwoori.global.config.BatchConfig;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest(classes = {EventJobConfig.class, SaveWinnerStepConfig.class, BatchConfig.class})
class EventJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void 이벤트_결과_DB_저장_테스트() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("eventId", 1L)
                .addDouble("random", Math.random())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals("COMPLETED", jobExecution.getStatus().toString());
    }
}
