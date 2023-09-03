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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class EventNotificationWriter implements ItemWriter<Long> {

    private final Long eventId;
    private final DataSource dataSource;

    private static final String NOTIFICATION_TYPE = "E";
    private static final String SAVE_NOTIFICATION_SQL = "INSERT INTO notification (user_id, type, created_at, is_checked, metadata) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void write(List<? extends Long> items) throws Exception {
        JdbcBatchItemWriter<Long> writer = new JdbcBatchItemWriterBuilder<Long>()
                .dataSource(dataSource)
                .sql(SAVE_NOTIFICATION_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Long>() {
                    @Override
                    public void setValues(Long item, PreparedStatement ps) throws SQLException {
                        ps.setLong(1, item);
                        ps.setString(2, NOTIFICATION_TYPE);
                        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setBoolean(4, false);
                        ps.setLong(5, eventId);
                    }
                })
                .build();

        writer.write(items);
    }
}
