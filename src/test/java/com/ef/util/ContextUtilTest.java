package com.ef.util;

import com.ef.test.TestEnvironment;
import io.micronaut.context.ApplicationContext;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class ContextUtilTest {

    private static final String ENV_TEST = ".env-test";

    @Test
    void getPropertyFromDotEnv() throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(ENV_TEST);
        assertThat(resource)
                .as("[GUARD] Should have found .env-test from test classpath")
                .isNotNull();

        String dotenvPath = new File(resource.getFile()).getAbsolutePath();
        try (ApplicationContext ctx = ContextUtil.getCtx(dotenvPath, TestEnvironment.NO_DB)) {
            assertThat(ctx).as("ctx")
                    .isNotNull()
                    .satisfies(_ctx -> {
                        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
                            softly.assertThat(_ctx.getProperty("jdbc.url", String.class).orElse(null))
                                    .as("jdbc.url")
                                    .isEqualTo("jdbc:mysql://127.0.0.1:3307/parser_test?allowPublicKeyRetrieval=true&useSSL=false");
                            softly.assertThat(_ctx.getProperty("jdbc.username", String.class).orElse(null))
                                    .as("jdbc.username")
                                    .isEqualTo("root");
                            softly.assertThat(_ctx.getProperty("jdbc.password", String.class).orElse(null))
                                    .as("jdbc.password")
                                    .isEqualTo("root");
                        }
                    });
        }
    }

    @Test
    void shouldStillWorkEvenWithMissingDotEnv() throws IOException {
        File dotenvFile = new File(".env-non-existent");
        assertThat(dotenvFile.exists())
                .as("[GUARD] Should NOT have found .env-non-existent from test classpath")
                .isFalse();

        String dotenvPath = dotenvFile.getAbsolutePath();
        try (ApplicationContext ctx = ContextUtil.getCtx(dotenvPath, TestEnvironment.NO_DB)) {
            assertThat(ctx).as("ctx")
                    .isNotNull()
                    .satisfies(_ctx -> {
                        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
                            softly.assertThat(_ctx.getProperty("jdbc.url", String.class).orElse(null))
                                    .as("jdbc.url")
                                    .isNull();
                            softly.assertThat(_ctx.getProperty("jdbc.username", String.class).orElse(null))
                                    .as("jdbc.username")
                                    .isNull();
                            softly.assertThat(_ctx.getProperty("jdbc.password", String.class).orElse(null))
                                    .as("jdbc.password")
                                    .isNull();
                        }
                    });
        }
    }

    @Test
    void givenNullDotenvPath() throws IOException {
        try (ApplicationContext ctx = ContextUtil.getCtx(null, TestEnvironment.NO_DB)) {
            assertThat(ctx).as("ctx")
                    .isNotNull()
                    .satisfies(_ctx -> {
                        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
                            softly.assertThat(_ctx.getProperty("jdbc.url", String.class).orElse(null))
                                    .as("jdbc.url")
                                    .isNull();
                            softly.assertThat(_ctx.getProperty("jdbc.username", String.class).orElse(null))
                                    .as("jdbc.username")
                                    .isNull();
                            softly.assertThat(_ctx.getProperty("jdbc.password", String.class).orElse(null))
                                    .as("jdbc.password")
                                    .isNull();
                        }
                    });
        }
    }
}