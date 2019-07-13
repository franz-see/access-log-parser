package ph.net.see.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ph.net.see.dto.Duration;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DurationConverterTest {

    private DurationConverter durationConverter;

    @BeforeEach
    void setup() {
        durationConverter = new DurationConverter();
    }

    @ParameterizedTest(name = "Test #{index}: Given String {0}, then convert to {1}")
    @MethodSource
    void shouldBeAbleToConvertStringIntoDurationEnum(String input, Duration expectedOutput) {
        Duration actualOutput = durationConverter.convert(input);
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }

    private static Stream<Arguments> shouldBeAbleToConvertStringIntoDurationEnum() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of("  ", null),
                Arguments.of(Duration.Hourly.name(), Duration.Hourly),
                Arguments.of(Duration.Daily.name(), Duration.Daily),
                Arguments.of(Duration.Hourly.name().toLowerCase(), Duration.Hourly),
                Arguments.of(Duration.Daily.name().toLowerCase(), Duration.Daily),
                Arguments.of(Duration.Hourly.name().toUpperCase(), Duration.Hourly),
                Arguments.of(Duration.Daily.name().toUpperCase(), Duration.Daily)
        );
    }
}