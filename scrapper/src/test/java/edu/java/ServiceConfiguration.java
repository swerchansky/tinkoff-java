package edu.java;

import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = "edu.java.domain")
public class ServiceConfiguration {
    @Bean
    @Primary
    public JpaLinkService jpaLinkService(JpaLinkRepository jpaLinkRepository, JpaChatRepository jpaChatRepository) {
        return new JpaLinkService(jpaChatRepository, jpaLinkRepository);
    }

    @Bean
    @Primary
    public JpaLinkUpdater jpaLinkUpdater(JpaLinkRepository jpaLinkRepository) {
        return new JpaLinkUpdater(jpaLinkRepository);
    }

    @Bean
    @Primary
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }
}
