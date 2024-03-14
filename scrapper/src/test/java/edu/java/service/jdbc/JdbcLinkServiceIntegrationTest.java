package edu.java.service.jdbc;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationEnvironmentConfiguration.class,
    DataBaseConfiguration.class,
    JdbcChatRepository.class,
    JdbcLinkRepository.class,
    JdbcLinkChatRepository.class,
    JdbcLinkService.class
})
class JdbcLinkServiceIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcLinkChatRepository jdbcLinkChatRepository;
    @Autowired
    private JdbcLinkService linkService;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link")
    public void add() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        List<LinkChat> linkChats = jdbcLinkChatRepository.findAll();

        assertThat(linkChats).isNotEmpty();
        assertThat(linkChats.getFirst().getUrl()).isEqualTo(URI.create("https://google.com"));
        assertThat(linkChats.getFirst().getChatId()).isEqualTo(1L);
        assertThat(jdbcLinkRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link")
    public void remove() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        linkService.remove(URI.create("https://google.com"), 1L);
        List<LinkChat> linkChats = jdbcLinkChatRepository.findAll();

        assertThat(linkChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find chats")
    public void findChats() {
        List<LinkChat> linkChats = List.of(
            new LinkChat(URI.create("https://google.com"), 1L, 0, 0, now(), now()),
            new LinkChat(URI.create("https://google.com"), 2L, 0, 0, now(), now())
        );

        linkChats.forEach(linkChat -> linkService.add(
            linkChat.getUrl(),
            linkChat.getChatId(),
            linkChat.getUpdatedDate(),
            linkChat.getStarCount(),
            linkChat.getAnswerCount()
        ));

        List<Chat> chats = linkService.findChats(URI.create("https://google.com"));

        assertThat(chats).isNotEmpty();
        assertThat(chats).hasSize(2);
        assertThat(chats).containsAll(linkChats.stream().map(linkChat -> new Chat(linkChat.getChatId())).toList());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find links")
    public void findLinks() {
        List<LinkChat> linkChats = List.of(
            new LinkChat(URI.create("https://google.com"), 1L, 0, 0, now(), now()),
            new LinkChat(URI.create("https://example.com"), 1L, 0, 0, now(), now())
        );

        linkChats.forEach(linkChat -> linkService.add(
            linkChat.getUrl(),
            linkChat.getChatId(),
            linkChat.getUpdatedDate(),
            linkChat.getStarCount(),
            linkChat.getAnswerCount()
        ));

        List<Link> links = linkService.findLinks(1L);

        assertThat(links).isNotEmpty();
        assertThat(links).hasSize(2);
        assertThat(links.stream().map(Link::getUrl).toList())
            .containsAll(linkChats.stream().map(LinkChat::getUrl).toList());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find link")
    public void findLink() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        Link link = linkService.findLink(URI.create("https://google.com"));

        assertThat(link).isNotNull();
        assertThat(link.getUrl()).isEqualTo(URI.create("https://google.com"));
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find link with id")
    public void findLinkWithId() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        LinkChat linkChat = linkService.findLinkWithId(URI.create("https://google.com"), 1L);

        assertThat(linkChat).isNotNull();
        assertThat(linkChat.getUrl()).isEqualTo(URI.create("https://google.com"));
        assertThat(linkChat.getChatId()).isEqualTo(1L);
    }
}
