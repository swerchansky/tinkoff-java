package edu.java.configuration.db;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jdbc.JdbcChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public ChatService chatService(JdbcChatRepository chatRepository) {
        return new JdbcChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
        JdbcLinkChatRepository linkChatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatRepository chatRepository
    ) {
        return new JdbcLinkService(linkChatRepository, linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JdbcLinkRepository linkRepository) {
        return new JdbcLinkUpdater(linkRepository);
    }
}
