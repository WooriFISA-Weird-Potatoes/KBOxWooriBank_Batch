package com.woorifisa.kboxwoori.batch.admin.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class SaveMemberCountReader implements ItemReader<Integer> {

    private final JdbcTemplate jdbcTemplate;

    private static final String MEMBER_COUNT_SQL = "SELECT count(*) FROM users";

    private boolean isRead = false;

    @Override
    public Integer read() {
        if (!isRead) {
            isRead = true;
            return jdbcTemplate.queryForObject(MEMBER_COUNT_SQL, Integer.class);
        }

        return null;
    }
}
