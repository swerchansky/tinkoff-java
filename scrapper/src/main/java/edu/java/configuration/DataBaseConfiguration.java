package edu.java.configuration;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class DataBaseConfiguration {
    private static final String USERNAME_PASSWORD = "postgres";
    private static final String CHECKED_DATE_COLUMN = "checked_date";
    private static final String UPDATED_DATE_COLUMN = "updated_date";
    private static final String ANSWER_COUNT_COLUMN = "answer_count";
    private static final String STAR_COUNT_COLUMN = "star_count";
    private static final String CHAT_ID_COLUMN = "chat_id";
    private static final String URL_COLUMN = "url";
    private static final String TIMEZONE = "Z";

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
                    ofInstant(rs.getTimestamp(UPDATED_DATE_COLUMN)),
                    ofInstant(rs.getTimestamp(CHECKED_DATE_COLUMN))
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
                    new Link(
                        new URI(rs.getString(URL_COLUMN)),
                        rs.getInt(STAR_COUNT_COLUMN),
                        rs.getInt(ANSWER_COUNT_COLUMN),
                        ofInstant(rs.getTimestamp(UPDATED_DATE_COLUMN)),
                        ofInstant(rs.getTimestamp(CHECKED_DATE_COLUMN))
                    ),
                    new Chat(rs.getLong(CHAT_ID_COLUMN))
                );
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private OffsetDateTime ofInstant(java.sql.Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return OffsetDateTime.of(localDateTime, ZoneOffset.of(TIMEZONE));
    }
}
