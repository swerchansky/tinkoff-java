package edu.java.migrations;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import edu.java.IntegrationEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DBMigrationTest extends IntegrationEnvironment {
    private static Statement statement;
    private final String SQL_INSERT = "INSERT INTO link (link,checked_date) VALUES ('http://google.com', now())";

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
        int result = statement.executeUpdate(SQL_INSERT);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("Select link")
    public void selectTest() throws SQLException {
        statement.executeUpdate(SQL_INSERT);
        var resultSet = statement.executeQuery("SELECT * FROM link");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("link")).isEqualTo("http://google.com");
    }
}
