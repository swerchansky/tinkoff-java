package edu.java.service.jpa;

import edu.java.IntegrationEnvironment;
import edu.java.ServiceConfiguration;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = LiquibaseAutoConfiguration.class)
@Import({IntegrationEnvironment.JpaConfig.class, ServiceConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaLinkServiceIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;

    @Test
    @DisplayName("add link")
    public void add() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        List<Link> links = linkService.findLinks(1L);
        List<Chat> chats = linkService.findChats(URI.create("https://google.com"));

        assertThat(links).isNotEmpty();
        assertThat(links.getFirst().getUrl()).isEqualTo(URI.create("https://google.com"));
        assertThat(chats.getFirst().getChatId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("remove link")
    public void remove() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        linkService.remove(URI.create("https://google.com"), 1L);
        List<Link> links = linkService.findLinks(1L);

        assertThat(links).isEmpty();
    }

    @Test
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
    @DisplayName("find link")
    public void findLink() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        Link link = linkService.findLink(URI.create("https://google.com"));

        assertThat(link).isNotNull();
        assertThat(link.getUrl()).isEqualTo(URI.create("https://google.com"));
    }

    @Test
    @DisplayName("find link with id")
    public void findLinkWithId() {
        linkService.add(URI.create("https://google.com"), 1L, OffsetDateTime.now(), 0, 0);
        LinkChat linkChat = linkService.findLinkWithId(URI.create("https://google.com"), 1L);

        assertThat(linkChat).isNotNull();
        assertThat(linkChat.getUrl()).isEqualTo(URI.create("https://google.com"));
        assertThat(linkChat.getChatId()).isEqualTo(1L);
    }
}
