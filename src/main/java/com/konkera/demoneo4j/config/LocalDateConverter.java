package com.konkera.demoneo4j.config;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.konkera.demoneo4j.utils.JsonUtil.DATE_PATTERN;

/**
 * @author konkera
 * @date 2021/7/27
 */
public class LocalDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(DATE_PATTERN));
    }
}
