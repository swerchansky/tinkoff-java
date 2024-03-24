package edu.java;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import static edu.java.utils.Time.getOffsetDateTime;

@Testcontainers
@SuppressWarnings("resource")
public abstract class IntegrationEnvironment {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    @SuppressWarnings("deprecation")
    private static void runMigrations(JdbcDatabaseContainer<?> container) {
        Path migrations = new File("").toPath().toAbsolutePath().getParent().resolve("migrations");

        try {
            Connection connection =
                DriverManager.getConnection(
                    container.getJdbcUrl(),
                    container.getUsername(),
                    container.getPassword()
                );
            ResourceAccessor changelogDirectory = new DirectoryResourceAccessor(migrations);
            PostgresDatabase db = new PostgresDatabase();
            db.setConnection(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("master.yaml", changelogDirectory, db);
            liquibase.update("");

            liquibase.close();
            changelogDirectory.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Configuration
    public static class JpaConfig {
        private static final String USERNAME_PASSWORD = "postgres";
        private static final String CHECKED_DATE_COLUMN = "checked_date";
        private static final String UPDATED_DATE_COLUMN = "updated_date";
        private static final String ANSWER_COUNT_COLUMN = "answer_count";
        private static final String STAR_COUNT_COLUMN = "star_count";
        private static final String CHAT_ID_COLUMN = "chat_id";
        private static final String URL_COLUMN = "url";

        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .url(POSTGRES.getJdbcUrl())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .build();
        }

        @Bean
        @Primary
        public JdbcOperations jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }

        @Bean
        public DataSourceConnectionProvider connectionProvider() {
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(testDataSource()));
        }

        @Bean
        public DSLContext dsl(DataSourceConnectionProvider connectionProvider) {
            return new DefaultDSLContext(new DefaultConfiguration()
                .set(connectionProvider)
                .set(SQLDialect.POSTGRES)
                .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
                .set(new Settings().withRenderQuotedNames(RenderQuotedNames.NEVER)
                    .withRenderNameCase(RenderNameCase.LOWER))
            );
        }

        @Bean
        public RowMapper<Chat> chatRowMapper() {
            return (rs, rowNum) -> new Chat(rs.getLong(CHAT_ID_COLUMN));
        }

        @Bean
        public RowMapper<Link> linkRowMapper() {
            return (rs, rowNum) -> {
                try {
                    return new Link(
                        new URI(rs.getString(URL_COLUMN)),
                        rs.getInt(STAR_COUNT_COLUMN),
                        rs.getInt(ANSWER_COUNT_COLUMN),
                        getOffsetDateTime(rs.getTimestamp(UPDATED_DATE_COLUMN)),
                        getOffsetDateTime(rs.getTimestamp(CHECKED_DATE_COLUMN))
                    );
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            };
        }

        @Bean
        public RowMapper<LinkChat> linkChatRowMapper() {
            return (rs, rowNum) -> {
                try {
                    return new LinkChat(
                        new URI(rs.getString(URL_COLUMN)),
                        rs.getLong(CHAT_ID_COLUMN),
                        rs.getInt(STAR_COUNT_COLUMN),
                        rs.getInt(ANSWER_COUNT_COLUMN),
                        getOffsetDateTime(rs.getTimestamp(UPDATED_DATE_COLUMN)),
                        getOffsetDateTime(rs.getTimestamp(CHECKED_DATE_COLUMN))
                    );
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    @TestConfiguration
    public static class IntegrationEnvironmentConfiguration {
        @Bean
        @Primary
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .url(POSTGRES.getJdbcUrl())
                .username(POSTGRES.getUsername())
                .password(POSTGRES.getPassword())
                .build();
        }

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new JdbcTransactionManager(dataSource);
        }
    }
}
