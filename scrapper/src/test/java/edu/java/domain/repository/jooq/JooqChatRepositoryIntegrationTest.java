package edu.java.domain.repository.jooq;

import edu.java.IntegrationEnvironment;
import edu.java.configuration.db.DataBaseConfiguration;
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
    IntegrationEnvironment.IntegrationEnvironmentConfiguration.class,
    DataBaseConfiguration.class,
    JooqChatRepository.class
})
class JooqChatRepositoryIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private JooqChatRepository jooqChatRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add chat")
    public void add() {
        Chat expected = jooqChatRepository.add(1L);
        List<Chat> actual = jooqChatRepository.findAll();

        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(expected);
        assertThat(actual.getFirst().getChatId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove chat")
    public void remove() {
        jooqChatRepository.add(1L);
        jooqChatRepository.remove(1L);
        List<Chat> actualChats = jooqChatRepository.findAll();

        assertThat(actualChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find chat by id")
    public void findById() {
        jooqChatRepository.add(1L);
        Chat expected = jooqChatRepository.add(5L);

        Chat actual = jooqChatRepository.findById(5L);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getChatId()).isEqualTo(5L);
    }
}
