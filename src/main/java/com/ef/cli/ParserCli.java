package com.ef.cli;

import com.ef.dto.Duration;
import com.ef.dto.ParseReport;
import com.ef.service.ParserService;
import com.ef.service.ParserServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.*;
import java.time.LocalDateTime;

/**
 * Some dummy production code
 */
@CommandLine.Command(name = "parser", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class,
        description = "Parses a given access log file and checks if a given IP makes more than a certain number of " +
                "requests for the given duration.")
public class ParserCli implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserCli.class);

    private ParserService parserService;

    @Inject
    void setParserService(ParserService parserService) {
        this.parserService = parserService;
    }

    @CommandLine.Option(
            names = { "--accesslog" },
            required = true,
            description = "The access log that would be parsed for IP search.")
    private File accesslog;

    @CommandLine.Option(
            names = { "--startDate" },
            required = true,
            converter = LocalDateTimeConverter.class,
            description = "The start date from which to search an IP from (inclusive). Format yyyy-MM-dd.HH:mm:SS. " +
                    "Example : 2017-01-01.13:00:00.")
    private LocalDateTime startDate;

    @CommandLine.Option(
            names = { "--duration" },
            required = true,
            converter = DurationConverter.class,
            description = "The duration on how far off from the startDate this will start looking for IP addresses. " +
                    "Acceptable values are 'hourly', and 'daily'")
    private Duration duration;

    @CommandLine.Option(
            names = { "--threshold" },
            required = true,
            description = "The amount of requests an IP made as seen from the accesslog.")
    private Integer threshold;

    @Override
    public void run() {
        try (Reader accesslogReader = new FileReader(accesslog)) {
            ParseReport parseReport = parserService.parse(accesslogReader, startDate, duration, threshold);
        } catch (FileNotFoundException e) {
            LOGGER.error("Unable to find {}.", accesslog.getAbsolutePath());
            System.exit(1);
        } catch (IOException e) {
            LOGGER.error("Unable to read {}.", accesslog.getAbsolutePath(), e);
            System.exit(1);
        } catch (Exception e) {
            LOGGER.error("Unable to parse {}.", accesslog.getAbsolutePath(), e);
            System.exit(1);
        }
    }
}
