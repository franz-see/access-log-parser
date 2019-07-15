package com.ef.test;

import javax.sql.DataSource;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryUtil {

    public static void assertDbEntryCount(DataSource dataSource, String tableName, int expectedCount)
            throws SQLException {
        String sqlQuery = "select count(*) as cnt from " + tableName;
        query(dataSource, sqlQuery, ps -> {}, resultSet -> {
            assertThat(resultSet.next()).as("has ResultSet").isTrue();
            int numberOfEntries = resultSet.getInt("cnt");
            assertThat(numberOfEntries).as(sqlQuery).isEqualTo(expectedCount);
        });
    }

    public static void query(
            DataSource dataSource,
            String sqlQuery,
            SqlConsumer<PreparedStatement> prepare,
            SqlConsumer<ResultSet> workWithResultSet)
            throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                prepare.accept(statement);
                workWithResultSet.accept(statement.executeQuery());
            }
        }
    }

    public static void deleteAll(DataSource dataSource, String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("delete from " + tableName);
            }
        }
    }

    @FunctionalInterface
    public interface SqlConsumer<T> {
        void accept(T t) throws SQLException;
    }
}
