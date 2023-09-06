package com.woorifisa.kboxwoori.batch.prediction;

import com.woorifisa.kboxwoori.batch.prediction.processor.CalcPredictionProcessor;
import com.woorifisa.kboxwoori.batch.prediction.reader.CalcPredictionReader;
import com.woorifisa.kboxwoori.batch.prediction.writer.PredictionNotificationWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.SavePredictionResultWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.UpdatePointHistoryWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.UpdatePointWriter;
import com.woorifisa.kboxwoori.global.DateJobParameter;
import com.woorifisa.kboxwoori.global.DateTimeJobParameter;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CalcPredictionStepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final DateJobParameter dateJobParameter;
    private final DateTimeJobParameter dateTimeJobParameter;

    @Bean
    @JobScope
    public Step calcPredictionStep() {
        return stepBuilderFactory.get("calcPredictionStep")
                .<Map.Entry<Object, Object>, UserPrediction>chunk(100)
                .reader(calcPredictionReader())
                .processor(calcPredictionProcessor())
                .writer(predictionCompositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Map.Entry<Object, Object>> calcPredictionReader() {
        final LocalDate date = dateJobParameter.getDate();
        return new CalcPredictionReader(redisTemplate, date);
    }

    @Bean
    @StepScope
    public ItemProcessor<Map.Entry<Object, Object>, UserPrediction> calcPredictionProcessor() {
        final LocalDate date = dateJobParameter.getDate();
        return new CalcPredictionProcessor(redisTemplate, jdbcTemplate, date);
    }

    @Bean
    @StepScope
    public CompositeItemWriter<UserPrediction> predictionCompositeItemWriter() {
        CompositeItemWriter<UserPrediction> itemWriter = new CompositeItemWriter<UserPrediction>();
        itemWriter.setDelegates(Arrays.asList(updatePointWriter(), updatePointHistoryWriter(), savePredictionResultWriter(), predictionNotificationWriter()));
        return itemWriter;
    }

    @StepScope
    public ItemWriter<UserPrediction> updatePointWriter() {
        return new UpdatePointWriter(dataSource);
    }

    @StepScope
    public ItemWriter<UserPrediction> updatePointHistoryWriter() {
        final LocalDateTime dateTime = dateTimeJobParameter.getDateTime();
        return new UpdatePointHistoryWriter(dataSource, dateTime);
    }

    @StepScope
    public ItemWriter<UserPrediction> savePredictionResultWriter() {
        final LocalDate date = dateJobParameter.getDate();
        return new SavePredictionResultWriter(dataSource, date);
    }

    @StepScope
    public ItemWriter<UserPrediction> predictionNotificationWriter() {
        final LocalDateTime dateTime = dateTimeJobParameter.getDateTime();
        return new PredictionNotificationWriter(dataSource, dateTime);
    }
}
