package ph.net.see.cli;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class VersionProviderTest {

    @Inject
    private VersionProvider versionProvider;

    @Test
    void testVersionReading() {
        assertThat(versionProvider.getVersion()).isEqualTo(new String[] {"parser test version"});
    }
}