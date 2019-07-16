package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.domain.PeriodSummary;
import com.ef.test.TestEnvironment;
import com.ef.util.ContextUtil;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ef.test.RepositoryUtil.assertDbEntryCount;
import static com.ef.test.RepositoryUtil.deleteAll;
import static com.ef.test.TestEnvironment.assumeMySqlIsUp;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessLogEntryRepositoryTest {

    private static AccessLogEntryRepository accessLogEntryRepository;
    private static DataSource dataSource;
    private static ApplicationContext ctx;

    @BeforeAll
    static void setup() throws IOException, SQLException {
        assumeMySqlIsUp();

        ctx = ContextUtil.getCtx(null, TestEnvironment.WITH_DB);
        accessLogEntryRepository = ctx.getBean(AccessLogEntryRepository.class);
        dataSource = ctx.getBean(DataSource.class);

        deleteAll(dataSource, "access_log_entry");
    }

    @AfterAll
    static void cleanup() {
        if (ctx != null) {
            ctx.stop();
        }
    }

    @Test
    @Order(1)
    void shouldBeAbleToSaveAccessLogEntry() throws SQLException {
        List<AccessLogEntry> accessLogEntries = asList(
                AccessLogEntry.builder()
                        .timestamp(LocalDateTime.of(2017, 1, 1, 23, 59, 59, 608 * 1000000))
                        .ipaddress("192.168.122.135")
                        .httpCall("GET / HTTP/1.1")
                        .httpStatusCode(200)
                        .userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0")
                        .build(),
                AccessLogEntry.builder()
                        .timestamp(LocalDateTime.of(2017, 1, 1, 0, 0, 21, 164 * 1000000))
                        .ipaddress("192.168.234.82")
                        .httpCall("GET / HTTP/1.1")
                        .httpStatusCode(200)
                        .userAgent("swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0")
                        .build()
        );

        accessLogEntryRepository.save(accessLogEntries);

        assertDbEntryCount(dataSource, "access_log_entry", 2);
    }

    @Test
    @Order(2)
    void shouldBeAbleToQuery() {
        LocalDateTime startDate = LocalDateTime.of(2017, 1, 1, 23, 59, 59);

        List<PeriodSummary> periodSummary = accessLogEntryRepository.findHourlyCount(startDate, 1);

        assertThat(periodSummary)
                .as("periodSummary")
                .isNotNull()
                .hasSize(1)
                .containsExactly(PeriodSummary.builder()
                        .startPeriod(startDate)
                        .endPeriod(startDate.plusHours(1))
                        .ipaddress("192.168.122.135")
                        .count(1L)
                        .comment("192.168.122.135 did 1 call(s) from 2017-01-01 23:59:59 to 2017-01-02 00:59:59")
                        .build());
    }

    @Test
    @Order(3)
    void shouldBeAbleToDeleteAll() throws SQLException {
        accessLogEntryRepository.deleteAll();

        assertDbEntryCount(dataSource, "access_log_entry", 0);
    }

}