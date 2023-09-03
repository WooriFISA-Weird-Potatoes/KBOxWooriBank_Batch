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
public class SaveQuizParticipantCountWriter implements ItemWriter<Long> {

    private final DataSource dataSource;

    private static final String SAVE_QUIZ_PARTICIPANT_COUNT_SQL = "UPDATE user_statistics SET quiz_participants = ? WHERE created_at = ?";

    @Override
    public void write(List<? extends Long> items) throws Exception {
        JdbcBatchItemWriter<Long> writer = new JdbcBatchItemWriterBuilder<Long>()
                .dataSource(dataSource)
                .sql(SAVE_QUIZ_PARTICIPANT_COUNT_SQL)
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Long>() {
                    @Override
                    public void setValues(Long item, PreparedStatement ps) throws SQLException {
                        ps.setInt(1, item.intValue());
                        ps.setDate(2, Date.valueOf(LocalDate.now()));
                    }
                })
                .build();

        writer.write(items);
    }
}
