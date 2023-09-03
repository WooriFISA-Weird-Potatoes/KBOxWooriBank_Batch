package com.woorifisa.kboxwoori.batch.event;

import com.woorifisa.kboxwoori.batch.event.processor.SaveWinnerProcessor;
import com.woorifisa.kboxwoori.batch.event.reader.SaveWinnerReader;
import com.woorifisa.kboxwoori.batch.event.writer.EventNotificationWriter;
import com.woorifisa.kboxwoori.batch.event.writer.SaveWinnerWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SaveWinnerStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Bean
    @JobScope
    public Step saveWinnerStep() {
        return stepBuilderFactory.get("saveWinnerStep")
                .<String, Long>chunk(100)
                .reader(saveWinnerReader(null))
                .processor(saveWinnerProcessor())
                .writer(compositeItemWriter(null))
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<String> saveWinnerReader(@Value("#{jobParameters['eventId']}") Long eventId) {
        return new SaveWinnerReader(redisTemplate, eventId);
    }

    @Bean
    @StepScope
    public ItemProcessor<String, Long> saveWinnerProcessor() {
        return new SaveWinnerProcessor(jdbcTemplate);
    }

    @Bean
    @StepScope
    public CompositeItemWriter<Long> compositeItemWriter(@Value("#{jobParameters['eventId']}") Long eventId) {
        CompositeItemWriter<Long> itemWriter = new CompositeItemWriter<Long>();
        itemWriter.setDelegates(Arrays.asList(saveWinnerWriter(eventId), eventNotificationWriter(eventId)));
        return itemWriter;
    }

    @StepScope
    public ItemWriter<Long> saveWinnerWriter(Long eventId) {
        return new SaveWinnerWriter(eventId, dataSource);
    }

    @StepScope
    public ItemWriter<Long> eventNotificationWriter(Long eventId) {
        return new EventNotificationWriter(eventId, dataSource);
    }
}
