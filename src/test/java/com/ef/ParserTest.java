package com.ef;

import com.ef.domain.PeriodSummary;
import com.ef.repository.PeriodSummaryRepository;
import io.micronaut.context.ApplicationContext;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ef.test.RepositoryUtil.query;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ParserTest {

    @BeforeAll
    static void setup() {
        System.setProperty("env", "withdb");
    }

    @Test
    @Order(1)
    void oneLineTest() throws Exception {
        int threshold = 100;
        File file = getFileFromClasspath("access-logs/basic.log");
        try (ApplicationContext ctx = Parser.run(new String[]{
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                format("--threshold=%d", threshold)
        })) {
            sanityTestPeriodSummaries(ctx, threshold);
        }
    }

    @Test
    @Order(2)
    void oneThousandLineTest() throws Exception {
        int threshold = 100;
        File file = getFileFromClasspath("access-logs/1000-lines.log");
        try (ApplicationContext ctx = Parser.run(new String[]{
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                format("--threshold=%d", threshold)
        })) {
            sanityTestPeriodSummaries(ctx, threshold);
        }
    }

    @Test
    @Order(3)
    void noAccesslogParameter() throws Exception {
        int threshold = 100;
        try (ApplicationContext ctx = Parser.run(new String[]{
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                format("--threshold=%d", threshold)
        })) {
            sanityTestPeriodSummaries(ctx, threshold);
        }
    }

    @Test
    @Order(4)
    void sanityTestOfHourly() throws Exception {
        int threshold = 100;
        File file = getFileFromClasspath("access-logs/full.log");
        try (ApplicationContext ctx = Parser.run(new String[]{
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.13:00:00",
                "--duration=hourly",
                format("--threshold=%d", threshold)
        })) {
            sanityTestPeriodSummaries(ctx, threshold);
        }
    }

    @Test
    @Order(5)
    void sanityTestOfDaily() throws Exception {
        int threshold = 100;
        File file = getFileFromClasspath("access-logs/full.log");
        try (ApplicationContext ctx = Parser.run(new String[]{
                format("--accesslog=%s", file.getAbsolutePath()),
                "--startDate=2017-01-01.00:00:00",
                "--duration=daily",
                format("--threshold=%d", threshold)
        })) {
            sanityTestPeriodSummaries(ctx, threshold);
        }
    }

    private void sanityTestPeriodSummaries(ApplicationContext ctx, int threshold) throws SQLException {
        PeriodSummaryRepository periodSummaryRepository = ctx.getBean(PeriodSummaryRepository.class);
        List<PeriodSummary> periodSummaries = periodSummaryRepository.findAll();
        DataSource dataSource = ctx.getBean(DataSource.class);

        try (AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions()) {
            for(PeriodSummary periodSummary : periodSummaries) {
                LocalDateTime startPeriod = periodSummary.getStartPeriod();
                LocalDateTime endPeriod = periodSummary.getEndPeriod();
                String ipaddress = periodSummary.getIpaddress();
                String sqlQuery = "select count(1) as cnt from access_log_entry " +
                        "where timestamp >= ? and timestamp <= ? and ip_address = ?";
                query(dataSource,
                        sqlQuery,
                        ps -> {
                            ps.setObject(1, startPeriod, JDBCType.TIMESTAMP);
                            ps.setObject(2, endPeriod, JDBCType.TIMESTAMP);
                            ps.setString(3, ipaddress);
                        },
                        resultSet -> {
                            assertThat(resultSet.next())
                                    .as("[GUARD] query for %s should have returned something", periodSummary)
                                    .isTrue();
                            softly.assertThat(resultSet.getLong("cnt"))
                                    .as("query count for %s", periodSummary)
                                    .isEqualTo(periodSummary.getCount())
                                    .isGreaterThanOrEqualTo(threshold);
                        });

            }
        }
    }

    private File getFileFromClasspath(String path) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        assertThat(resource).as("[GUARD] should have found %s from the classpath.", path).isNotNull();
        return new File(resource.getFile());
    }
}