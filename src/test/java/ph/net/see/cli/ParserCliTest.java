package ph.net.see.cli;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import ph.net.see.dto.Duration;
import ph.net.see.service.ParserService;
import picocli.CommandLine;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ParserCliTest {

    @Mock
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
        File accesslog = new File(Thread.currentThread().getContextClassLoader().getResource("access.log").getFile());
        String accesslogAbsolutePath = accesslog.getAbsolutePath();

        String[] args = new String[]{
                "--accesslog=" + accesslogAbsolutePath,
                "--startDate=2017-01-02.13:03:04",
                "--duration=hourly",
                "--threshold=100"
        };
        CommandLine.run(command, args);

        ArgumentCaptor<File> accesslogCaptor = ArgumentCaptor.forClass(File.class);
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
                    .satisfies(file -> softly.assertThat(file.getAbsolutePath())
                            .as("accesslog absolutepath")
                            .isEqualTo(accesslogAbsolutePath));
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
