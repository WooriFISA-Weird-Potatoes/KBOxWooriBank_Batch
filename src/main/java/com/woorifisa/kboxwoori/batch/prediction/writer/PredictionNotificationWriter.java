package com.woorifisa.kboxwoori.batch.prediction.writer;

import com.woorifisa.kboxwoori.batch.prediction.UserPrediction;
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
public class PredictionNotificationWriter implements ItemWriter<UserPrediction> {

    private final DataSource dataSource;

    private static final String NOTIFICATION_TYPE = "P";
    private static final String SAVE_NOTIFICATION_SQL = "INSERT INTO notification (user_id, type, created_at, is_checked, metadata) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void write(List<? extends UserPrediction> items) throws Exception {
        JdbcBatchItemWriter<UserPrediction> writer = new JdbcBatchItemWriterBuilder<UserPrediction>()
                .dataSource(dataSource)
                .sql(SAVE_NOTIFICATION_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<UserPrediction>() {
                    @Override
                    public void setValues(UserPrediction item, PreparedStatement ps) throws SQLException {
                        ps.setLong(1, item.getId());
                        ps.setString(2, NOTIFICATION_TYPE);
                        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                        ps.setBoolean(4, false);
                        ps.setLong(5, item.getExtraPoints());
                    }
                })
                .build();

        writer.write(items);
    }
}
