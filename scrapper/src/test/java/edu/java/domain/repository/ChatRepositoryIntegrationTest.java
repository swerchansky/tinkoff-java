package edu.java.domain.repository;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
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
    ChatRepository.class
})
class ChatRepositoryIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add chat")
    public void add() {
        Chat expected = chatRepository.add(1L);
        List<Chat> actual = chatRepository.findAll();

        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(expected);
        assertThat(actual.getFirst().getChatId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove chat")
    public void remove() {
        chatRepository.add(1L);
        chatRepository.remove(1L);
        List<Chat> actualChats = chatRepository.findAll();

        assertThat(actualChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find chat by id")
    public void findById() {
        chatRepository.add(1L);
        Chat expected = chatRepository.add(5L);

        Chat actual = chatRepository.findById(5L);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getChatId()).isEqualTo(5L);
    }
}
