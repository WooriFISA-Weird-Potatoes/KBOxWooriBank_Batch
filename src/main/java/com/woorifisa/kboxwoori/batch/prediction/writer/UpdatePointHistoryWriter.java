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
public class UpdatePointHistoryWriter implements ItemWriter<UserPrediction> {

    private final DataSource dataSource;
    private final LocalDateTime dateTime;

    private static final String UPDATE_POINT_HISTORY_SQL = "INSERT INTO point_history (user_id, status_code, point, created_at) VALUES (?, ?, ?, ?)";
    private static final String POINT_HISTORY_STATUS = "SAVE";

    @Override
    public void write(List<? extends UserPrediction> items) throws Exception {
        JdbcBatchItemWriter<UserPrediction> writer = new JdbcBatchItemWriterBuilder<UserPrediction>()
                .dataSource(dataSource)
                .sql(UPDATE_POINT_HISTORY_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<UserPrediction>() {
                    @Override
                    public void setValues(UserPrediction item, PreparedStatement ps) throws SQLException {
                        ps.setLong(1, item.getId());
                        ps.setString(2, POINT_HISTORY_STATUS);
                        ps.setInt(3, item.getExtraPoints());
                        ps.setTimestamp(4, Timestamp.valueOf(dateTime));
                    }
                })
                .build();

        writer.write(items);
    }
}
