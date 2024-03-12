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
        String sql = "INSERT INTO link (url,checked_date) VALUES ('http://google.com', now())";
        int result = statement.executeUpdate(sql);
        assertThat(result).isEqualTo(1);
    }
}
