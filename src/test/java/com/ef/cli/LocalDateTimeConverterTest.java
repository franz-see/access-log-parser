package com.ef.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class LocalDateTimeConverterTest {

    private LocalDateTimeConverter localDateTimeConverter;

    @BeforeEach
    void setup() {
        localDateTimeConverter = new LocalDateTimeConverter();
    }

    @Test
    void convertToLocalDateTime() throws Exception {
        String input = "2017-01-02.13:03:04";
        LocalDateTime actualOutput = localDateTimeConverter.convert(input);

        assertThat(actualOutput)
                .as("converted LocalDateTime of %s", input)
                .isEqualTo(LocalDateTime.of(
                        LocalDate.of(2017, 1, 2),
                        LocalTime.of(13, 3, 4)
                ));
    }
}