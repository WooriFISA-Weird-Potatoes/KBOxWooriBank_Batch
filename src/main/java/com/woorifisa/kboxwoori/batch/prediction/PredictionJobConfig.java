package com.woorifisa.kboxwoori.batch.prediction;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class PredictionJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final Step calcPredictionStep;

    @Bean
    public Job predictionJob() {
        return jobBuilderFactory.get("predictionJob")
                .start(calcPredictionStep)
                .build();
    }

}
