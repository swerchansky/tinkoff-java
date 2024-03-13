package edu.java.scheduler;

import edu.java.client.BotClient;
import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.GithubRepositoryResponse;
import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.dto.StackOverflowQuestionsResponse;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.utils.parser.LinkParser;
import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(name = "app.scheduler.enable")
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;
    private final LinkService linkService;
    private final BotClient botClient;
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.debug("Updating links...");
        List<Link> oldLinks = linkUpdater.getOldLinks();
        oldLinks.forEach(link -> {
            URI url = link.getUrl();
            List<Long> chatIds = linkService.findChats(url).stream().map(Chat::getChatId).toList();
            switch (LinkParser.parseLinkInfo(url)) {
                case GithubLinkParserResult result ->
                    githubClient.getRepositoryInfo(result.getOwner(), result.getRepositoryName()).subscribe(
                        onGithubSuccessResponse(url, chatIds),
                        onError(link)
                    );
                case StackOverflowlinkParserResult result ->
                    stackOverflowClient.getQuestionInfo(result.getId()).subscribe(
                        onStackOverflowSuccessResponse(url, chatIds),
                        onError(link)
                    );
                case null, default -> onError(link).accept(new IllegalArgumentException("Unsupported link type"));
            }
        });
        linkUpdater.updateCheckedDate(oldLinks);
    }

    private static Consumer<Throwable> onError(Link link) {
        return throwable -> log.error("Error while updating link: " + link.getUrl(), throwable);
    }

    private Consumer<GithubRepositoryResponse> onGithubSuccessResponse(URI url, List<Long> chatIds) {
        return repository -> {
            if (!repository.getUpdatedAt().isEqual(linkService.findLink(url).getUpdatedDate())) {
                linkUpdater.updateUpdatedDate(linkService.findLink(url), repository.getUpdatedAt());
                LinkUpdateRequest request = new LinkUpdateRequest(url, repository.getName(), chatIds);
                botClient.update(request).subscribe();
            }
        };
    }

    private Consumer<StackOverflowQuestionsResponse> onStackOverflowSuccessResponse(
        URI url,
        List<Long> chatIds
    ) {
        return question -> {
            OffsetDateTime currentUpdatedDate = question.getQuestions().getFirst().getUpdatedAt();
            if (!currentUpdatedDate.isEqual(linkService.findLink(url).getUpdatedDate())) {
                linkUpdater.updateUpdatedDate(linkService.findLink(url), currentUpdatedDate);
                LinkUpdateRequest request =
                    new LinkUpdateRequest(url, question.questions.getFirst().getTitle(), chatIds);
                botClient.update(request).subscribe();
            }
        };
    }
}
