package com.woorifisa.kboxwoori.batch.admin;

import com.woorifisa.kboxwoori.global.config.BatchConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest(classes = {AdminJobConfig.class, SaveMemberCountStepConfig.class,
        SavePredictionParticipantCountStepConfig.class, SaveQuizParticipantCountStepConfig.class, BatchConfig.class})
class AdminJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void 통계_결과_저장_테스트() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertEquals("COMPLETED", jobExecution.getStatus().toString());
    }
}