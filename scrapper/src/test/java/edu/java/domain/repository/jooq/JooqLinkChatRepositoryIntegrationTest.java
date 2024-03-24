package edu.java.domain.repository.jooq;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.db.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.time.OffsetDateTime;
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
    JooqLinkChatRepository.class,
    JooqChatRepository.class,
    JooqLinkRepository.class
})
class JooqLinkChatRepositoryIntegrationTest extends IntegrationEnvironment {
    private static final URI URL = URI.create("http://google.com");
    @Autowired
    private JooqLinkChatRepository jooqLinkChatRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link chat")
    public void add() {
        Link link = jooqLinkRepository.add(URL, OffsetDateTime.now(), 0, 0);
        Chat chat = jooqChatRepository.add(6L);
        LinkChat expected = jooqLinkChatRepository.add(URL, 6L);
        List<LinkChat> actualLinkChats = jooqLinkChatRepository.findAll();

        assertThat(actualLinkChats).hasSize(1);
        assertThat(actualLinkChats).containsExactly(expected);
        LinkChat actual = actualLinkChats.getFirst();
        assertThat(actual.getChatId()).isEqualTo(chat.getChatId());
        assertThat(actual.getUrl()).isEqualTo(link.getUrl());
        assertThat(actual.getCheckedDate()).isEqualTo(link.getCheckedDate());
        assertThat(actual.getUpdatedDate()).isEqualTo(link.getUpdatedDate());
        assertThat(actual.getAnswerCount()).isEqualTo(link.getAnswerCount());
        assertThat(actual.getStarCount()).isEqualTo(link.getStarCount());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link chat")
    public void remove() {
        jooqLinkRepository.add(URL, OffsetDateTime.now(), 0, 0);
        jooqChatRepository.add(1L);
        jooqLinkChatRepository.add(URL, 1L);
        jooqLinkChatRepository.remove(URL, 1L);
        List<LinkChat> actualLinkChats = jooqLinkChatRepository.findAll();

        assertThat(actualLinkChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find link chat by unknown url and chat id")
    public void findUnknown() {
        LinkChat actual = jooqLinkChatRepository.find(URL, 1L);

        assertThat(actual).isNull();
    }
}
