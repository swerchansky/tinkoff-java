package edu.java.configuration.db;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.net.URISyntaxException;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import static edu.java.utils.Time.getOffsetDateTime;

@Configuration
public class DataBaseConfiguration {
    private static final String USERNAME_PASSWORD = "postgres";
    private static final String CHECKED_DATE_COLUMN = "checked_date";
    private static final String UPDATED_DATE_COLUMN = "updated_date";
    private static final String ANSWER_COUNT_COLUMN = "answer_count";
    private static final String STAR_COUNT_COLUMN = "star_count";
    private static final String CHAT_ID_COLUMN = "chat_id";
    private static final String URL_COLUMN = "url";

    @Value("${spring.datasource.url}")
    private static String url = "jdbc:postgresql://localhost:5432/scrapper";

    @Value("${spring.datasource.username}")
    private static String username = USERNAME_PASSWORD;

    @Value("${spring.datasource.password}")
    private static String password = USERNAME_PASSWORD;

    @Value("${spring.datasource.driver-class-name}")
    private static String driver = "org.postgresql.Driver";

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName(driver)
            .build();
    }

    @Bean
    public JdbcOperations jdbcOperations(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DSLContext dsl(DataSourceConnectionProvider connectionProvider) {
        return new DefaultDSLContext(new DefaultConfiguration()
            .set(connectionProvider)
            .set(SQLDialect.POSTGRES)
            .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
            .set(new Settings().withRenderQuotedNames(RenderQuotedNames.NEVER).withRenderNameCase(RenderNameCase.LOWER))
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
