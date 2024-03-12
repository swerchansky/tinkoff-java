package edu.java.configuration;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import static java.time.OffsetDateTime.ofInstant;

@Configuration
public class DataBaseConfiguration {
    @Value("${spring.datasource.url}")
    private static String url = "jdbc:postgresql://localhost:5432/scrapper";

    @Value("${spring.datasource.username}")
    private static String username = "postgres";

    @Value("${spring.datasource.password}")
    private static String password = "postgres";

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
        return (rs, rowNum) -> new Chat(rs.getLong("chat_id"));
    }

    @Bean
    public RowMapper<Link> linkRowMapper() {
        return (rs, rowNum) -> {
            try {
                return new Link(
                    new URI(rs.getString("url")),
                    ofInstant(rs.getTimestamp("checked_date").toInstant(), ZoneId.of("UTC"))
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
                        new URI(rs.getString("url")),
                        ofInstant(rs.getTimestamp("checked_date").toInstant(), ZoneId.of("UTC"))
                    ),
                    new Chat(rs.getLong("chat_id"))
                );
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
