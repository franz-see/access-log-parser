package com.ef.repository;

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
import static java.util.Collections.singletonList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PeriodSummaryRepositoryTest {

    private static PeriodSummaryRepository periodSummaryRepository;
    private static DataSource dataSource;
    private static ApplicationContext ctx;

    @BeforeAll
    static void setup() throws IOException, SQLException {
        assumeMySqlIsUp();

        ctx = ContextUtil.getCtx(null, TestEnvironment.WITH_DB);
        periodSummaryRepository = ctx.getBean(PeriodSummaryRepository.class);
        dataSource = ctx.getBean(DataSource.class);

        deleteAll(dataSource, "period_summary");
    }

    @AfterAll
    static void cleanup() {
        if (ctx != null) {
            ctx.stop();
        }
    }

    @Test
    @Order(1)
    void shouldBeAbleToSavePeriodSummary() throws SQLException {
        LocalDateTime startDate = LocalDateTime.of(2017, 1, 1, 23, 59, 59);
        List<PeriodSummary> periodSummaries = singletonList(
                PeriodSummary.builder()
                        .startPeriod(startDate)
                        .endPeriod(startDate.plusHours(1))
                        .ipaddress("192.168.122.135")
                        .count(1L)
                        .comment("192.168.122.135 did 1 call(s) from 2017-01-01 23:59:59 to 2017-01-02 00:59:59")
                        .build()
        );

        periodSummaryRepository.save(periodSummaries);

        assertDbEntryCount(dataSource, "period_summary", 1);
    }

    @Test
    @Order(3)
    void shouldBeAbleToDeleteAll() throws SQLException {
        periodSummaryRepository.deleteAll();

        assertDbEntryCount(dataSource, "period_summary", 0);
    }
}