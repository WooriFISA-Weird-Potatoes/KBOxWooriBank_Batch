package com.woorifisa.kboxwoori.batch.prediction.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CalcPredictionReader implements ItemReader<Map.Entry<Object, Object>> {

    private final RedisTemplate<String, String> redisTemplate;
    private final LocalDate date;

    private static final int COUNT = 100;
    private static final String PREDICTION_KEY = "prediction:";

    private Cursor<Map.Entry<Object, Object>> cursor;

    @PostConstruct
    public void init() {
        ScanOptions options = ScanOptions.scanOptions().count(COUNT).build();
        this.cursor = redisTemplate.opsForHash().scan(PREDICTION_KEY + date.minusDays(1), options);
    }

    @Override
    public Map.Entry<Object, Object> read() {
        log.info("Item Reader read()");
        return cursor.hasNext() ? cursor.next() : null;
    }
}
