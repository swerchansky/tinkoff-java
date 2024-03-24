package edu.java.configuration.db;

import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public ChatService chatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository
    ) {
        return new JpaLinkService(chatRepository, linkRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JpaLinkRepository linkRepository) {
        return new JpaLinkUpdater(linkRepository);
    }
}
