package edu.java.service.jdbc;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import java.net.URI;
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
    ChatRepository.class,
    LinkRepository.class,
    LinkChatRepository.class,
    JdbcLinkService.class
})
class JdbcLinkServiceIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinkChatRepository linkChatRepository;
    @Autowired
    private JdbcLinkService linkService;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link")
    public void add() {
        linkService.add(URI.create("https://google.com"), 1L);
        List<LinkChat> linkChats = linkChatRepository.findAll();

        assertThat(linkChats).isNotEmpty();
        assertThat(linkChats.getFirst().getLink().getUrl()).isEqualTo(URI.create("https://google.com"));
        assertThat(linkChats.getFirst().getChat().getChatId()).isEqualTo(1L);
        assertThat(linkRepository.findAll()).isNotEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link")
    public void remove() {
        linkService.add(URI.create("https://google.com"), 1L);
        linkService.remove(URI.create("https://google.com"), 1L);
        List<LinkChat> linkChats = linkChatRepository.findAll();

        assertThat(linkChats).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find chats")
    public void findChats() {
        List<LinkChat> linkChats = List.of(
            new LinkChat(new Link(URI.create("https://google.com"), now(), now()), new Chat(1L)),
            new LinkChat(new Link(URI.create("https://google.com"), now(), now()), new Chat(2L))
        );

        linkChats.forEach(linkChat -> linkService.add(
            linkChat.getLink().getUrl(),
            linkChat.getChat().getChatId()
        ));

        List<Chat> chats = linkService.findChats(URI.create("https://google.com"));

        assertThat(chats).isNotEmpty();
        assertThat(chats).hasSize(2);
        assertThat(chats).containsAll(linkChats.stream().map(LinkChat::getChat).toList());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find links")
    public void findLinks() {
        List<LinkChat> linkChats = List.of(
            new LinkChat(new Link(URI.create("https://google.com"), now(), now()), new Chat(1L)),
            new LinkChat(new Link(URI.create("https://example.com"), now(), now()), new Chat(1L))
        );

        linkChats.forEach(linkChat -> linkService.add(
            linkChat.getLink().getUrl(),
            linkChat.getChat().getChatId()
        ));

        List<Link> links = linkService.findLinks(1L);

        assertThat(links).isNotEmpty();
        assertThat(links).hasSize(2);
        assertThat(links.stream().map(Link::getUrl).toList())
            .containsAll(linkChats.stream().map(LinkChat::getLink).map(Link::getUrl).toList());
    }
}
