package com.woorifisa.kboxwoori.batch.admin.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class SavePredictionParticipantCountReader implements ItemReader<Long> {

    private final RedisTemplate<String, String> redisTemplate;
    private final LocalDate date;

    private static final String PREDICTION_KEY = "prediction:";

    private boolean isRead = false;

    @Override
    public Long read() {
        if (!isRead) {
            isRead = true;
            return redisTemplate.opsForHash().size(PREDICTION_KEY + date.minusDays(1));
        }

        return null;
    }
}
