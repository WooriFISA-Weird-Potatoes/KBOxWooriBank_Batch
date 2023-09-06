package com.woorifisa.kboxwoori.batch.admin.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class SaveQuizParticipantCountReader implements ItemReader<Long> {

    private final RedisTemplate<String, String> redisTemplate;
    private final LocalDate date;

    private static final String QUIZ_KEY = "quiz:participant:";

    private boolean isRead = false;

    @Override
    public Long read() {
        if (!isRead) {
            isRead = true;
            return redisTemplate.opsForSet().size(QUIZ_KEY + date.minusDays(1));
        }

        return null;
    }
}
