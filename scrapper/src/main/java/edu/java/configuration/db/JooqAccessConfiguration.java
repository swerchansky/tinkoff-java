package edu.java.configuration.db;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqLinkChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jooq.JooqChatService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public ChatService chatService(JooqChatRepository chatRepository) {
        return new JooqChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
        JooqLinkChatRepository linkChatRepository,
        JooqLinkRepository linkRepository,
        JooqChatRepository chatRepository
    ) {
        return new JooqLinkService(linkChatRepository, linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JooqLinkRepository linkRepository) {
        return new JooqLinkUpdater(linkRepository);
    }
}
