package com.konkera.demoneo4j.config;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.konkera.demoneo4j.utils.JsonUtil.DATETIME_PATTERN;

/**
 * @author konkera
 * @date 2021/7/27
 */
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
    }
}
