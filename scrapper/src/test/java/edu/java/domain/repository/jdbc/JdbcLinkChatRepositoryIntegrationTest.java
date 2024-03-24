package edu.java.domain.repository.jdbc;

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
    JdbcLinkChatRepository.class,
    JdbcChatRepository.class,
    JdbcLinkRepository.class
})
class JdbcLinkChatRepositoryIntegrationTest extends IntegrationEnvironment {
    private static final URI URL = URI.create("http://google.com");
    @Autowired
    private JdbcLinkChatRepository jdbcLinkChatRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link chat")
    public void add() {
        Link link = jdbcLinkRepository.add(URL, OffsetDateTime.now(), 0, 0);
        Chat chat = jdbcChatRepository.add(1L);
        LinkChat expected = jdbcLinkChatRepository.add(URL, 1L);
        List<LinkChat> actualLinkChats = jdbcLinkChatRepository.findAll();

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
        jdbcLinkRepository.add(URL, OffsetDateTime.now(), 0, 0);
        jdbcChatRepository.add(1L);
        jdbcLinkChatRepository.add(URL, 1L);
        jdbcLinkChatRepository.remove(URL, 1L);
        List<LinkChat> actualLinkChats = jdbcLinkChatRepository.findAll();

        assertThat(actualLinkChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find link chat by unknown url and chat id")
    public void findUnknown() {
        LinkChat actual = jdbcLinkChatRepository.find(URL, 1L);

        assertThat(actual).isNull();
    }
}
