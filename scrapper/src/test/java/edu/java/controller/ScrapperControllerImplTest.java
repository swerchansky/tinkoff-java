package edu.java.controller;

import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.GithubRepositoryResponse;
import edu.java.client.dto.StackOverflowQuestionsResponse;
import edu.java.client.dto.StackOverflowQuestionsResponse.QuestionResponse;
import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScrapperControllerImplTest {
    @Mock
    private LinkService linkService;
    @Mock
    private ChatService chatService;
    @Mock
    private GithubClient githubClient;
    @Mock
    private StackOverflowClient stackOverflowClient;
    private ScrapperControllerImpl scrapperController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scrapperController = new ScrapperControllerImpl(linkService, chatService, githubClient, stackOverflowClient);
    }

    @Test
    @DisplayName("addChat should register chat")
    void addChatShouldRegisterChat() {
        long id = 1L;
        scrapperController.addChat(id);
        verify(chatService, times(1)).register(id);
    }

    @Test
    @DisplayName("getLinks should return links")
    void getLinksShouldReturnLinks() {
        long id = 1L;
        Link link = new Link(URI.create("http://example.com"), 0, 0, OffsetDateTime.now(), OffsetDateTime.now());
        when(linkService.findLinks(id)).thenReturn(Collections.singletonList(link));
        assertThat(scrapperController.getLinks(id))
            .isEqualTo(new ListLinksResponse(Collections.singletonList(new LinkResponse(link.getUrl())), 1));
    }

    @Test
    @DisplayName("addLink should add link")
    void addLinkShouldAddLink() {
        long id = 1L;
        URI uriGithub = URI.create("http://github.com/owner/repo");
        AddLinkRequest addGithubLinkRequest = new AddLinkRequest(uriGithub);
        when(githubClient.getRepositoryInfo(any(), any()))
            .thenReturn(Mono.just(new GithubRepositoryResponse("repo", "owner", 0, OffsetDateTime.now())));

        URI uriStackoverflow = URI.create("http://stackoverflow.com/questions/1");
        AddLinkRequest addStackoverflowLinkRequest = new AddLinkRequest(uriStackoverflow);
        when(stackOverflowClient.getQuestionInfo(1L))
            .thenReturn(Mono.just(new StackOverflowQuestionsResponse(List.of(new QuestionResponse(
                "",
                "",
                OffsetDateTime.now(),
                0
            )))));

        scrapperController.addLink(id, addGithubLinkRequest);
        verify(linkService, times(1)).add(eq(uriGithub), eq(id), any(), eq(0), eq(null));

        scrapperController.addLink(id, addStackoverflowLinkRequest);
        verify(linkService, times(1)).add(eq(uriStackoverflow), eq(id), any(), eq(null), eq(0));
    }

    @Test
    @DisplayName("deleteLink should delete link")
    void deleteLinkShouldDeleteLink() {
        long id = 1L;
        URI uri = URI.create("http://github.com/owner/repo");
        when(linkService.findLinkWithId(uri, id)).thenReturn(
            new LinkChat(
                uri,
                id,
                0,
                0,
                OffsetDateTime.now(),
                OffsetDateTime.now()
            )
        );
        scrapperController.deleteLink(id, new RemoveLinkRequest(uri));
        verify(linkService, times(1)).remove(uri, id);
    }
}
