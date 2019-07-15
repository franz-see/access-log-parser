package com.ef.cli;

import com.ef.test.TestEnvironment;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(environments = TestEnvironment.NO_DB)
class VersionProviderTest {

    private VersionProvider versionProvider;

    @SuppressWarnings("unused")
    @Inject
    public void setVersionProvider(VersionProvider versionProvider) {
        this.versionProvider = versionProvider;
    }

    @Test
    void testVersionReading() {
        assertThat(versionProvider.getVersion()).isEqualTo(new String[] {"parser test version"});
    }
}