package com.ef.util;

import com.ef.domain.AccessLogEntry;
import com.ef.service.ParserServiceException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static java.lang.String.format;

public class LineParser {

    public static final DateTimeFormatter TIMESTAMP_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final int EXPECTED_NUMBER_OF_SECTIONS = 5;

    public static AccessLogEntry parse(int lineNumber, String line) {
        if (line == null || line.trim().length() == 0) {
            return null;
        }
        String[] sections = line.split("\\|(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        checkNumberOfSections(lineNumber, line, sections);
        sections = clean(sections);
        LocalDateTime timestamp = toTimestamp(lineNumber, line, sections[0]);
        Integer httpStatusCode = toHttpStatusCode(lineNumber, line, sections[3]);
        return AccessLogEntry.builder()
                .timestamp(timestamp)
                .ipaddress(sections[1])
                .httpCall(sections[2])
                .httpStatusCode(httpStatusCode)
                .userAgent(sections[4])
                .build();
    }

    private static String[] clean(String[] sections) {
        return Arrays.stream(sections)
                .map(section -> {
                    if (section.startsWith("\"") && section.endsWith("\"")) {
                        return section.substring(1, section.length() - 1);
                    } else {
                        return section;
                    }
                }).toArray(String[]::new);
    }

    private static void checkNumberOfSections(int lineNumber, String line, String[] sections) {
        if (sections.length != EXPECTED_NUMBER_OF_SECTIONS) {
            String message = format(
                    "Line #%d: Expected to have %d number of split sections by '|' but got %d: %s", lineNumber,
                    EXPECTED_NUMBER_OF_SECTIONS, sections.length, line);
            throw new ParserServiceException(message);
        }
    }

    private static LocalDateTime toTimestamp(int lineNumber, String line, String timestampRaw) {
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timestampRaw, TIMESTAMP_FORMATER);
        } catch (Exception e) {
            throw new ParserServiceException(format("Line #%d: Unable to convert %s into timestamp: %s", lineNumber,
                    timestampRaw, line), e);
        }
        return timestamp;
    }

    private static Integer toHttpStatusCode(int lineNumber, String line, String httpStatusCodeRaw) {
        Integer httpStatusCode;
        try {
            httpStatusCode = Integer.valueOf(httpStatusCodeRaw);
        } catch (Exception e) {
            throw new ParserServiceException(format("Line #%d: Unable to convert %s into http status code : %s", lineNumber,
                    httpStatusCodeRaw, line), e);
        }
        return httpStatusCode;
    }
}
