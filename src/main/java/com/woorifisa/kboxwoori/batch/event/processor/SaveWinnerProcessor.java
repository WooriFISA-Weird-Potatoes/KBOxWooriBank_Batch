package com.woorifisa.kboxwoori.batch.event.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class SaveWinnerProcessor implements ItemProcessor<String, Long> {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_USER_SQL = "SELECT id FROM users WHERE user_id = ?";

    @Override
    public Long process(String item) {
        return jdbcTemplate.queryForObject(FIND_USER_SQL, Long.class, item);
    }
}
