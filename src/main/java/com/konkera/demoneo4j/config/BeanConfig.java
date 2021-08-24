package com.konkera.demoneo4j.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkera.demoneo4j.utils.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author konkera
 * @date 2021/7/27
 */
@Configuration
public class BeanConfig {

    /**
     * json全局序列化与反序列化配置
     */
    @Bean
    public ObjectMapper initObjectMapper() {
        return JsonUtil.getObjectMapper();
    }

    /**
     * String --> LocalDate
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new LocalDateConverter();
    }

    /**
     * String --> LocalDatetime
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new LocalDateTimeConverter();
    }
}
