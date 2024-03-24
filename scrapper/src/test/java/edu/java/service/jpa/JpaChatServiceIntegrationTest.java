package edu.java.service.jpa;

import edu.java.IntegrationEnvironment;
import edu.java.ServiceConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jpa.JpaChatRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = LiquibaseAutoConfiguration.class)
@Import({IntegrationEnvironment.JpaConfig.class, ServiceConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaChatServiceIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private JpaChatRepository jpaChatRepository;
    @Autowired
    private JpaChatService chatService;

    @Test
    @DisplayName("register chat")
    public void register() {
        assertThat(jpaChatRepository.findAll()).isEmpty();

        chatService.register(1L);
        List<Chat> chats =
            jpaChatRepository.findAll().stream().map(chatEntity -> new Chat(chatEntity.getChatId())).toList();

        assertThat(chats).isNotEmpty();
        assertThat(chats.getFirst().getChatId()).isEqualTo(1L);
    }
}
