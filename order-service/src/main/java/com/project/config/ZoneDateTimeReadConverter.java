package com.project.config;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class ZoneDateTimeReadConverter implements Converter<Date, LocalDateTime> {
    @Override
    public LocalDateTime convert(Date source) {
        return source.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
