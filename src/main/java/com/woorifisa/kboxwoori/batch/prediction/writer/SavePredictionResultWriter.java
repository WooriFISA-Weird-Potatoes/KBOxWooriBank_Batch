package com.woorifisa.kboxwoori.batch.prediction.writer;

import com.woorifisa.kboxwoori.batch.prediction.UserPrediction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SavePredictionResultWriter implements ItemWriter<UserPrediction> {

    private final DataSource dataSource;
    private final LocalDate date;

    private static final String SAVE_PREDICTION_RESULT_SQL = "INSERT INTO prediction_history (user_id, is_correct, created_at) VALUES (?, ?, ?)";

    @Override
    public void write(List<? extends UserPrediction> items) throws Exception {
        JdbcBatchItemWriter<UserPrediction> writer = new JdbcBatchItemWriterBuilder<UserPrediction>()
                .dataSource(dataSource)
                .sql(SAVE_PREDICTION_RESULT_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<UserPrediction>() {
                    @Override
                    public void setValues(UserPrediction item, PreparedStatement ps) throws SQLException {
                        ps.setLong(1, item.getId());
                        ps.setBoolean(2, item.getExtraPoints() != 0);
                        ps.setDate(3, Date.valueOf(date.minusDays(1)));
                    }
                })
                .build();

        writer.write(items);
    }
}
