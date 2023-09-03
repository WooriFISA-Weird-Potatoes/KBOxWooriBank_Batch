package com.woorifisa.kboxwoori.batch.admin;

import com.woorifisa.kboxwoori.batch.admin.reader.SaveMemberCountReader;
import com.woorifisa.kboxwoori.batch.admin.writer.SaveMemberCountWriter;
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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SaveMemberCountStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Bean
    @JobScope
    public Step saveMemberCountStep() {
        return stepBuilderFactory.get("saveMemberCountStep")
                .<Integer, Integer>chunk(1)
                .reader(saveMemberCountReader())
                .writer(saveMemberCountWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Integer> saveMemberCountReader() {
        return new SaveMemberCountReader(jdbcTemplate);
    }

    @Bean
    @StepScope
    public ItemWriter<Integer> saveMemberCountWriter() {
        return new SaveMemberCountWriter(dataSource);
    }
}
