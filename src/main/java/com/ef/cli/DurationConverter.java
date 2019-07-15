package com.ef.cli;

import com.ef.dto.Duration;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.Objects;

public class DurationConverter implements CommandLine.ITypeConverter<Duration> {

    @Override
    public Duration convert(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(Duration.values())
                .filter(duration -> Objects.equals(duration.name().toLowerCase(), value.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
}
