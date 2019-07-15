package com.ef.test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryUtil {

    public static void assertDbEntryCount(DataSource dataSource, String tableName, int expectedCount) throws SQLException {
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

    public static void deleteAll(DataSource dataSource, String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("delete from " + tableName);
            }
        }
    }
}
