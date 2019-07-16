package com.ef.cli;

import com.ef.dto.Duration;
import com.ef.service.ParserService;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ParserCliTest {

    ParserService parserService;

    private ParserCli command;

    @BeforeEach
    void setup() {
        parserService = mock(ParserService.class);
        command = new ParserCli();
        command.setParserService(parserService);
    }

    @Test
    void shouldBeAbleToParseArgumentsProperly() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("access-logs/full.log");
        assertThat(resource)
                .as("[GUARD] full.log should be searchable from the classpath")
                .isNotNull();

        File accesslog = new File(resource.getFile());
        String accesslogAbsolutePath = accesslog.getAbsolutePath();

        String[] args = new String[]{
                "--accesslog=" + accesslogAbsolutePath,
                "--startDate=2017-01-02.13:03:04",
                "--duration=hourly",
                "--threshold=100"
        };
        CommandLine.run(command, args);

        ArgumentCaptor<Reader> accesslogCaptor = ArgumentCaptor.forClass(Reader.class);
        ArgumentCaptor<LocalDateTime> startDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);
        ArgumentCaptor<Integer> thresholdCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(parserService).parse(
                accesslogCaptor.capture(),
                startDateCaptor.capture(),
                durationCaptor.capture(),
                thresholdCaptor.capture()
        );

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(accesslogCaptor.getValue())
                    .as("accesslog")
                    .isNotNull()
                    .isInstanceOf(FileReader.class);
            softly.assertThat(startDateCaptor.getValue())
                    .as("startDate")
                    .isEqualTo(LocalDateTime.of(
                            LocalDate.of(2017, 1, 2),
                            LocalTime.of(13, 3, 4)
                    ));
            softly.assertThat(durationCaptor.getValue())
                    .as("duration")
                    .isEqualTo(Duration.Hourly);
            softly.assertThat(thresholdCaptor.getValue())
                    .as("threshold")
                    .isEqualTo(100);
        }
    }

}
