package edu.java.domain.repository;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
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
    LinkChatRepository.class,
    ChatRepository.class,
    LinkRepository.class
})
class LinkChatRepositoryIntegrationTest extends IntegrationEnvironment {
    private static final URI URL = URI.create("http://google.com");
    @Autowired
    private LinkChatRepository linkChatRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link chat")
    public void add() {
        Link link = linkRepository.add(URL);
        Chat chat = chatRepository.add(1L);
        LinkChat expected = linkChatRepository.add(URL, 1L);
        List<LinkChat> actualLinkChats = linkChatRepository.findAll();

        assertThat(actualLinkChats).hasSize(1);
        assertThat(actualLinkChats).containsExactly(expected);
        LinkChat actual = actualLinkChats.getFirst();
        assertThat(actual.getLink()).isEqualTo(link);
        assertThat(actual.getChat()).isEqualTo(chat);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link chat")
    public void remove() {
        linkRepository.add(URL);
        chatRepository.add(1L);
        linkChatRepository.add(URL, 1L);
        linkChatRepository.remove(URL, 1L);
        List<LinkChat> actualLinkChats = linkChatRepository.findAll();

        assertThat(actualLinkChats).isEmpty();
    }
}
