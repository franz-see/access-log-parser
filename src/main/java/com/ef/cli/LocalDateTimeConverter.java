package com.ef.cli;

import picocli.CommandLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements CommandLine.ITypeConverter<LocalDateTime> {

    public static final String FORMAT = "yyyy-MM-dd'.'HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT);

    @Override
    public LocalDateTime convert(String value) throws Exception {
        return LocalDateTime.parse(value, formatter);
    }
}
