package com.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Slf4j
public class ZoneDateTimeWriteConverter implements Converter<LocalDateTime, Date> {

    @Override
    public Date convert(LocalDateTime source) {
        return new Date(source.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
