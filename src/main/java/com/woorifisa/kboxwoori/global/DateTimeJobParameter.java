package com.woorifisa.kboxwoori.global;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DateTimeJobParameter {

    private LocalDateTime dateTime;

    @Value("#{jobParameters[dateTime]}")
    public void setDateTime(String dateTime) {
        if (StringUtils.hasText(dateTime)) {
            this.dateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
