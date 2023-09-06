package com.woorifisa.kboxwoori.global;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class DateJobParameter {

    private LocalDate date;

    @Value("#{jobParameters[date]}")
    public void setDate(String date) {
        if (StringUtils.hasText(date)) {
            this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }
}
