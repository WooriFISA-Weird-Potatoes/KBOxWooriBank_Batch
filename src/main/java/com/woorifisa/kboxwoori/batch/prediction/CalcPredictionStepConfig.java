package com.woorifisa.kboxwoori.batch.prediction;

import com.woorifisa.kboxwoori.batch.prediction.processor.CalcPredictionProcessor;
import com.woorifisa.kboxwoori.batch.prediction.reader.CalcPredictionReader;
import com.woorifisa.kboxwoori.batch.prediction.writer.PredictionNotificationWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.SavePredictionResultWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.UpdatePointHistoryWriter;
import com.woorifisa.kboxwoori.batch.prediction.writer.UpdatePointWriter;
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

    //TODO: job parameter date 추가
    @Bean
    @JobScope
    public Step calcPredictionStep() {
        return stepBuilderFactory.get("calcPredictionStep")
                .<Map.Entry<Object, Object>, UserPrediction>chunk(100)
                .reader(calcPredictionReader())
                .processor(calcPredictionProcessor())
                .writer(compositeItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Map.Entry<Object, Object>> calcPredictionReader() {
        return new CalcPredictionReader(redisTemplate);
    }

    @Bean
    @StepScope
    public ItemProcessor<Map.Entry<Object, Object>, UserPrediction> calcPredictionProcessor() {
        return new CalcPredictionProcessor(redisTemplate, jdbcTemplate);
    }

    @Bean
    @StepScope
    public CompositeItemWriter<UserPrediction> compositeItemWriter() {
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
        return new UpdatePointHistoryWriter(dataSource);
    }

    @StepScope
    public ItemWriter<UserPrediction> savePredictionResultWriter() {
        return new SavePredictionResultWriter(dataSource);
    }

    @StepScope
    public ItemWriter<UserPrediction> predictionNotificationWriter() {
        return new PredictionNotificationWriter(dataSource);
    }
}
