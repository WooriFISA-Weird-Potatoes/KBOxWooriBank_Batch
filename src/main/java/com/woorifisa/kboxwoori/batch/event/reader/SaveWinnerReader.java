package com.woorifisa.kboxwoori.batch.event.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
public class SaveWinnerReader implements ItemReader<String> {

    private final RedisTemplate<String, String> redisTemplate;
    private final Long eventId;

    private static final String EVENT_KEY = "event:winner:";

    private Cursor<String> cursor;

    @PostConstruct
    public void init() {
        ScanOptions options = ScanOptions.scanOptions().count(100).build();
        cursor = redisTemplate.opsForSet().scan(EVENT_KEY + eventId, options);
    }

    @Override
    public String read() {
        return cursor.hasNext() ? cursor.next() : null;
    }
}
