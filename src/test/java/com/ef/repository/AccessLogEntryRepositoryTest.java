package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.test.TestEnvironment;
import com.ef.util.ContextUtil;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.ef.test.TestEnvironment.assumeMySqlIsUp;
import static org.assertj.core.api.Assertions.assertThat;

class AccessLogEntryRepositoryTest {

    private static AccessLogEntryRepository accessLogEntryRepository;

    @BeforeAll
    static void setup() throws IOException {
        assumeMySqlIsUp();

        ApplicationContext ctx = ContextUtil.getCtx(null, TestEnvironment.WITH_DB);
        accessLogEntryRepository = ctx.getBean(AccessLogEntryRepository.class);
    }

    @Test
    void shouldBeAbleToSaveAccessLogEntry() {
        AccessLogEntry sampleAccessLog = AccessLogEntry.builder()
                .timestamp(LocalDateTime.of(2017, 1, 1, 23, 59, 59, 608 * 1000000))
                .ipaddress("192.168.122.135")
                .httpCall("GET / HTTP/1.1")
                .httpStatusCode(200)
                .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0")
                .build();

        AccessLogEntry savedEntry = accessLogEntryRepository.save(sampleAccessLog);

        assertThat(savedEntry)
                .as("saved AccessLogEntry")
                .isNotNull()
                .extracting(AccessLogEntry::getId)
                .as("saved AccessLogEntry#id")
                .isNotNull();
    }
}