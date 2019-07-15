package com.ef.repository;

import com.ef.domain.AccessLogEntry;
import com.ef.test.TestEnvironment;
import com.ef.util.ContextUtil;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static com.ef.test.TestEnvironment.assumeMySqlIsUp;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccessLogEntryRepositoryTest {

    private static AccessLogEntryRepository accessLogEntryRepository;
    private static DataSource dataSource;

    @BeforeAll
    static void setup() throws IOException, SQLException {
        assumeMySqlIsUp();

        ApplicationContext ctx = ContextUtil.getCtx(null, TestEnvironment.WITH_DB);
        accessLogEntryRepository = ctx.getBean(AccessLogEntryRepository.class);
        dataSource = ctx.getBean(DataSource.class);

        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("delete from access_log_entry");
            }
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

        int expectedCount = 2;
        assertDbEntryCount(expectedCount, "access_log_entry");
    }

    @Test
    @Order(3)
    void shouldBeAbleToDeleteAll() throws SQLException {
        accessLogEntryRepository.deleteAll();

        assertDbEntryCount(0, "access_log_entry");
    }

    private void assertDbEntryCount(int expectedCount, String tableName) throws SQLException {
        String sqlQuery = "select count(*) as cnt from " + tableName;
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                assertThat(resultSet.next()).as("has ResultSet").isTrue();
                int numberOfEntries = resultSet.getInt("cnt");
                assertThat(numberOfEntries).as(sqlQuery).isEqualTo(expectedCount);
            }
        }
    }
}