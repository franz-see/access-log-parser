package com.ef.service;

import com.ef.domain.AccessLogEntry;
import com.ef.dto.Duration;
import com.ef.dto.ParseReport;
import com.ef.util.LineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;

@Singleton
public class ParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserService.class);

    public ParseReport parse(Reader accesslog, LocalDateTime startDate, Duration duration, int threshold) {
        try {
            LOGGER.info("Parsing {} from startDate {}, with duration {}, and threshold {}", accesslog, startDate, duration, threshold);
            BufferedReader bufferedReader = new BufferedReader(accesslog);
            int lineNumber = 0;
            String line = bufferedReader.readLine();
            while (line != null) {
                lineNumber++;
                line = bufferedReader.readLine();
                AccessLogEntry accessLogEntry = LineParser.parse(lineNumber, line);
            }
        } catch (IOException e) {
            throw new ParserServiceException("Unable to read accesslog", e);
        } catch (Exception e) {
            throw new ParserServiceException("Unable to parse accesslog", e);
        }
        return null;
    }
}
