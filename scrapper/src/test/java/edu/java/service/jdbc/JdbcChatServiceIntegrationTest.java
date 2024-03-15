package edu.java.service.jdbc;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationEnvironmentConfiguration.class,
    DataBaseConfiguration.class,
    JdbcChatRepository.class,
    JdbcChatService.class
})
class JdbcChatServiceIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcChatService chatService;

    @Test
    @Transactional
    @Rollback
    @DisplayName("register chat")
    public void register() {
        assertThat(jdbcChatRepository.findAll()).isEmpty();

        chatService.register(1L);
        List<Chat> chats = jdbcChatRepository.findAll();

        assertThat(chats).isNotEmpty();
        assertThat(chats.getFirst().getChatId()).isEqualTo(1L);
    }
}
