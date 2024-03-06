package edu.java.migrations;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkInsertTest extends IntegrationEnvironment {
    private static Statement statement;

    @BeforeAll
    public static void setUp() throws Exception {
        statement = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        ).createStatement();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        statement.close();
    }

    @Test
    @DisplayName("Insert link")
    public void insertTest() throws SQLException {
        int result = statement.executeUpdate("INSERT INTO link (link,type_id) VALUES ('http://google.com', 1)");
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("Select link")
    public void selectTest() throws SQLException {
        statement.executeUpdate("INSERT INTO link (link,type_id) VALUES ('http://google.com', 1)");
        var resultSet = statement.executeQuery("SELECT * FROM link");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("link")).isEqualTo("http://google.com");
        assertThat(resultSet.getInt("type_id")).isEqualTo(1);
    }
}
