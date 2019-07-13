package ph.net.see.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ph.net.see.dto.Duration;
import ph.net.see.dto.ParseReport;

import javax.inject.Singleton;
import java.io.File;
import java.time.LocalDateTime;

@Singleton
public class ParserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserService.class);

    public ParseReport parse(File accesslog, LocalDateTime startDate, Duration duration, int threshold) {
        LOGGER.info("Parsing {} from startDate {}, with duration {}, and threshhold {}", accesslog, startDate, duration, threshold);
        return null;
    }
}
