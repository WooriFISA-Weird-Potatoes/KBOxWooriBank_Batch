package com.woorifisa.kboxwoori.batch.admin.writer;

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
public class SaveMemberCountWriter implements ItemWriter<Integer> {

    private final DataSource dataSource;
    private final LocalDate date;

    private static final String SAVE_MEMBER_COUNT_SQL = "INSERT INTO user_statistics (total_users, created_at) VALUES (?, ?)";

    @Override
    public void write(List<? extends Integer> items) throws Exception {
        JdbcBatchItemWriter<Integer> writer = new JdbcBatchItemWriterBuilder<Integer>()
                .dataSource(dataSource)
                .sql(SAVE_MEMBER_COUNT_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Integer>() {
                    @Override
                    public void setValues(Integer item, PreparedStatement ps) throws SQLException {
                        ps.setInt(1, item);
                        ps.setDate(2, Date.valueOf(date));
                    }
                })
                .build();

        writer.write(items);
    }
}
