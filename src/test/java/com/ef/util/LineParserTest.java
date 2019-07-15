package com.ef.util;

import com.ef.domain.AccessLogEntry;
import com.ef.service.ParserServiceException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineParserTest {

    @ParameterizedTest(name = "Test #{index}: Given String {0}, then convert to {1}")
    @MethodSource
    void shouldBeAbleToConvertLineIntoAccessLogEntry(String input, AccessLogEntry expectedOutput) {
        AccessLogEntry actualOutput = LineParser.parse(0, input);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    @ParameterizedTest(name = "Test #{index}: Given String {0}, then report error")
    @MethodSource
    void shouldBeAbleToReportErrorOnImproperLines(String input) {
        assertThatThrownBy(() -> LineParser.parse(0, input))
                .isInstanceOf(ParserServiceException.class);
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> shouldBeAbleToConvertLineIntoAccessLogEntry() throws IOException {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of("  ", null),
                Arguments.of(
                        readFromClasspath("access-logs/basic.log"),
                        AccessLogEntry.builder()
                                .timestamp(LocalDateTime.of(2017, 1, 1, 23, 59, 59, 608 * 1000000))
                                .ipaddress("192.168.122.135")
                                .httpCall("GET / HTTP/1.1")
                                .httpStatusCode(200)
                                .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0")
                                .build())
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> shouldBeAbleToReportErrorOnImproperLines() throws IOException {
        return Stream.of(
                Arguments.of(readFromClasspath("access-logs/improper-number-of-sections.log")),
                Arguments.of(readFromClasspath("access-logs/improper-http-status-code.log")),
                Arguments.of(readFromClasspath("access-logs/improper-timestamp.log"))
        );
    }

    private static String readFromClasspath(String path) throws IOException {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (inputStream == null) {
                return null;
            }
            java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
            return s.hasNext() ? s.next() : null;
        }
    }

}