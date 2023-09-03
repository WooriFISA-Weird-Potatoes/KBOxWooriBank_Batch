package com.woorifisa.kboxwoori.batch.event.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SaveWinnerWriter implements ItemWriter<Long> {

    private final Long eventId;
    private final DataSource dataSource;

    private static final String SAVE_WINNER_SQL = "INSERT INTO winner (event_id, user_id) VALUES (?, ?)";

    @Override
    public void write(List<? extends Long> items) throws Exception {
        JdbcBatchItemWriter<Long> writer = new JdbcBatchItemWriterBuilder<Long>()
                .dataSource(dataSource)
                .sql(SAVE_WINNER_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Long>() {
                    @Override
                    public void setValues(Long item, PreparedStatement ps) throws SQLException {
                        log.info("eventId = {}, userId = {}", eventId, item);
                        ps.setLong(1, eventId);
                        ps.setLong(2, item);
                    }
                })
                .build();

        writer.write(items);
    }
}
