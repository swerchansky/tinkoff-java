package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.GithubRepositoryResponse;
import edu.java.client.dto.StackOverflowQuestionsResponse;
import edu.java.domain.dto.Link;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import static java.time.OffsetDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LinkUpdaterSchedulerTest {
    @Mock
    private LinkUpdater linkUpdater;
    @Mock
    private LinkService linkService;
    @Mock
    private BotClient botClient;
    @Mock
    private GithubClient githubClient;
    @Mock
    private StackOverflowClient stackOverflowClient;
    private LinkUpdaterScheduler linkUpdaterScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        linkUpdaterScheduler = new LinkUpdaterScheduler(
            linkUpdater,
            linkService,
            botClient,
            githubClient,
            stackOverflowClient
        );
    }

    @Test
    @DisplayName("Should update Github links successfully")
    public void shouldUpdateGithubLinksSuccessfully() {
        Link link = new Link(URI.create("https://github.com/user/repo"), now(), now());
        when(linkUpdater.getOldLinks()).thenReturn(List.of(link));
        when(linkService.findChats(any())).thenReturn(Collections.emptyList());
        when(linkService.findLink(any())).thenReturn(new Link(
            URI.create("https://github.com/user/repo"),
            now(),
            now()
        ));
        when(githubClient.getRepositoryInfo(
            anyString(),
            anyString()
        )).thenReturn(Mono.just(new GithubRepositoryResponse("", "", now())));

        linkUpdaterScheduler.update();
        verify(botClient, times(1)).update(any());
    }

    @Test
    @DisplayName("Should update StackOverflow links successfully")
    public void shouldUpdateStackOverflowLinksSuccessfully() {
        Link link = new Link(URI.create("https://stackoverflow.com/questions/123"), now(), now());
        when(linkUpdater.getOldLinks()).thenReturn(List.of(link));
        when(linkService.findChats(any())).thenReturn(Collections.emptyList());
        when(linkService.findLink(any())).thenReturn(new Link(
            URI.create("https://stackoverflow.com/questions/123"),
            now(),
            now()
        ));
        when(stackOverflowClient.getQuestionInfo(anyLong()))
            .thenReturn(Mono.just(new StackOverflowQuestionsResponse(List.of(
                new StackOverflowQuestionsResponse.QuestionResponse("", "", now())
            ))));

        linkUpdaterScheduler.update();
        verify(botClient, times(1)).update(any());
    }

    @Test
    @DisplayName("Should handle unsupported link type")
    public void shouldHandleUnsupportedLinkType() {
        Link link = new Link(URI.create("https://unsupported.com/link"), now(), now());
        when(linkUpdater.getOldLinks()).thenReturn(List.of(link));

        linkUpdaterScheduler.update();
        verify(botClient, times(0)).update(any());
    }
}
