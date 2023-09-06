package com.woorifisa.kboxwoori.batch.admin;

import com.woorifisa.kboxwoori.batch.admin.reader.SavePredictionParticipantCountReader;
import com.woorifisa.kboxwoori.batch.admin.writer.SavePredictionParticipantCountWriter;
import com.woorifisa.kboxwoori.global.DateJobParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SavePredictionParticipantCountStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final RedisTemplate<String, String> redisTemplate;
    private final DataSource dataSource;
    private final DateJobParameter dateJobParameter;

    @Bean
    @JobScope
    public Step savePredictionParticipantCountStep() {
        return stepBuilderFactory.get("savePredictionParticipantCountStep")
                .<Long, Long>chunk(1)
                .reader(savePredictionParticipantCountReader())
                .writer(savePredictionParticipantCountWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Long> savePredictionParticipantCountReader() {
        final LocalDate date = dateJobParameter.getDate();
        return new SavePredictionParticipantCountReader(redisTemplate, date);
    }

    @Bean
    @StepScope
    public ItemWriter<Long> savePredictionParticipantCountWriter() {
        final LocalDate date = dateJobParameter.getDate();
        return new SavePredictionParticipantCountWriter(dataSource, date);
    }
}
