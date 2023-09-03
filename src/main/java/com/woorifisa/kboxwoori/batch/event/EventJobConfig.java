package com.woorifisa.kboxwoori.batch.event;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class EventJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step saveWinnerStep;

    @Bean
    public Job eventJob() {
        return jobBuilderFactory.get("eventJob")
                .start(saveWinnerStep)
                .build();
    }

}

