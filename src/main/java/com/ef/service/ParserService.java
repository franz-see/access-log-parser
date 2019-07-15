package com.ef.service;

import com.ef.domain.AccessLogEntry;
import com.ef.dto.Duration;
import com.ef.dto.ParseReport;
import com.ef.repository.AccessLogEntryRepository;
import com.ef.util.LineParser;
import io.micronaut.context.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Singleton
public class ParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserService.class);

    @Value("${application.parser.size:1000}")
    private int parserSize;

    @Inject
    private AccessLogEntryRepository accessLogEntryRepository;

    public ParseReport parse(Reader accesslog, LocalDateTime startDate, Duration duration, int threshold) {
        LOGGER.info("Parsing {} from startDate {}, with duration {}, and threshold {} (parse size = {}).",
                accesslog, startDate, duration, threshold, parserSize);
        try {
            if (accesslog != null) {
                LOGGER.info("Deleting all entries in AccessLogEntry table.");
                accessLogEntryRepository.deleteAll();

                LOGGER.info("Reading accesslog file and entering into the database.");
                BufferedReader bufferedReader = new BufferedReader(accesslog);
                int lineNumber = 0;
                String line = bufferedReader.readLine();
                List<String> bufferedLines = new LinkedList<>();
                while (line != null) {
                    bufferedLines.add(line);
                    if (bufferedLines.size() >= parserSize) {
                        lineNumber = process(lineNumber, bufferedLines);
                        bufferedLines.clear();
                    }
                    line = bufferedReader.readLine();
                }
                if (!bufferedLines.isEmpty()) {
                    process(lineNumber, bufferedLines);
                    bufferedLines.clear();
                }
            }

            return null;
        } catch (IOException e) {
            throw new ParserServiceException("Unable to read accesslog", e);
        } catch (Exception e) {
            throw new ParserServiceException("Unable to process accesslog", e);
        }
    }

    private int process(int lastLineNumberSeen, List<String> lines) {
        LOGGER.info("Saving logs from line {} up to line {}", lastLineNumberSeen+1, lastLineNumberSeen + lines.size());
        AtomicInteger lineNumber = new AtomicInteger(lastLineNumberSeen);
        List<AccessLogEntry> accessLogEntries = lines.stream()
                .filter(Objects::nonNull)
                .map(line -> LineParser.parse(lineNumber.incrementAndGet(), line))
                .collect(Collectors.toList());
        accessLogEntryRepository.save(accessLogEntries);
        return lineNumber.get();
    }
}
