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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UpdatePointWriter implements ItemWriter<UserPrediction> {

    private final DataSource dataSource;

    private static final String UPDATE_POINT_SQL = "UPDATE users SET point = point + ? WHERE id = ?";

    @Override
    public void write(List<? extends UserPrediction> items) throws Exception {
        JdbcBatchItemWriter<UserPrediction> writer = new JdbcBatchItemWriterBuilder<UserPrediction>()
                .dataSource(dataSource)
                .sql(UPDATE_POINT_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<UserPrediction>() {
                    @Override
                    public void setValues(UserPrediction item, PreparedStatement ps) throws SQLException {
                        ps.setInt(1, item.getExtraPoints());
                        ps.setLong(2, item.getId());
                    }
                })
                .build();

        writer.write(items);
    }
}
