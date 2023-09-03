package com.woorifisa.kboxwoori.batch.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AdminJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step saveMemberCountStep;
    private final Step savePredictionParticipantCountStep;
    private final Step saveQuizParticipantCountStep;

    @Bean
    public Job adminJob() {
        return jobBuilderFactory.get("adminJob")
                .start(saveMemberCountStep)
                .next(savePredictionParticipantCountStep)
                .next(saveQuizParticipantCountStep)
                .build();
    }
}
