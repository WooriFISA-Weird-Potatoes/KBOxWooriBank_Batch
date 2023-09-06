package com.woorifisa.kboxwoori.global.config;

import com.woorifisa.kboxwoori.global.DateJobParameter;
import com.woorifisa.kboxwoori.global.DateTimeJobParameter;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JobParameterConfig {

    @Bean
    @JobScope
    public DateJobParameter dateJobParameter() {
        return new DateJobParameter();
    }

    @Bean
    @JobScope
    public DateTimeJobParameter dateTimeJobParameter() {
        return new DateTimeJobParameter();
    }
}
