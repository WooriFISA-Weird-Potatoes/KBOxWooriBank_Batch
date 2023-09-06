package com.woorifisa.kboxwoori.batch.admin;

import com.woorifisa.kboxwoori.batch.admin.reader.SaveQuizParticipantCountReader;
import com.woorifisa.kboxwoori.batch.admin.writer.SaveQuizParticipantCountWriter;
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
public class SaveQuizParticipantCountStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final RedisTemplate<String, String> redisTemplate;
    private final DataSource dataSource;
    private final DateJobParameter dateJobParameter;

    @Bean
    @JobScope
    public Step saveQuizParticipantCountStep() {
        return stepBuilderFactory.get("saveQuizParticipantCountStep")
                .<Long, Long>chunk(1)
                .reader(saveQuizParticipantCountReader())
                .writer(saveQuizParticipantCountWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Long> saveQuizParticipantCountReader() {
        final LocalDate date = dateJobParameter.getDate();
        return new SaveQuizParticipantCountReader(redisTemplate, date);
    }

    @Bean
    @StepScope
    public ItemWriter<Long> saveQuizParticipantCountWriter() {
        final LocalDate date = dateJobParameter.getDate();
        return new SaveQuizParticipantCountWriter(dataSource, date);
    }
}
