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
    private static final String DEFAULT_MESSAGE = "new changes detected";
    private final LinkUpdater jdbcLinkUpdater;
    private final LinkService jdbcLinkService;
    private final BotClient botClient;
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.debug("Updating links...");
        List<Link> oldLinks = jdbcLinkUpdater.getOldLinks();
        oldLinks.forEach(link -> {
            URI url = link.getUrl();
            List<Long> chatIds = jdbcLinkService.findChats(url).stream().map(Chat::getChatId).toList();
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
        jdbcLinkUpdater.updateCheckedDate(oldLinks);
    }

    private static Consumer<Throwable> onError(Link link) {
        return throwable -> log.error("Error while updating link: " + link.getUrl(), throwable);
    }

    private Consumer<GithubRepositoryResponse> onGithubSuccessResponse(URI url, List<Long> chatIds) {
        return repository -> {
            Link link = jdbcLinkService.findLink(url);
            String message = null;
            if (repository.getStarCount() > link.getStarCount()) {
                message = "new star added, current count: " + repository.getStarCount();
            } else if (!repository.getUpdatedAt().isEqual(link.getUpdatedDate())) {
                message = DEFAULT_MESSAGE;
            }
            if (message != null) {
                updateLink(link, null, repository.getStarCount(), repository.getUpdatedAt());
                sendMessage(url, chatIds, message);
            }
        };
    }

    private Consumer<StackOverflowQuestionsResponse> onStackOverflowSuccessResponse(
        URI url,
        List<Long> chatIds
    ) {
        return question -> {
            Link link = jdbcLinkService.findLink(url);
            String message = null;
            if (question.getQuestions().isEmpty()) {
                return;
            }
            StackOverflowQuestionsResponse.QuestionResponse currentQuestion = question.getQuestions().getFirst();
            if (currentQuestion.getAnswerCount() > link.getAnswerCount()) {
                message = "new answer added, current count: " + currentQuestion.getAnswerCount();
            } else if (!currentQuestion.getUpdatedAt().isEqual(link.getUpdatedDate())) {
                message = DEFAULT_MESSAGE;
            }
            if (message != null) {
                updateLink(link, currentQuestion.getAnswerCount(), null, currentQuestion.getUpdatedAt());
                sendMessage(url, chatIds, message);
            }
        };
    }

    private void sendMessage(URI url, List<Long> chatIds, String message) {
        LinkUpdateRequest request = new LinkUpdateRequest(url, message, chatIds);
        botClient.update(request).subscribe();
    }

    private void updateLink(Link link, Integer answerCount, Integer starCount, OffsetDateTime updatedAt) {
        jdbcLinkUpdater.updateAnswerCount(link, answerCount);
        jdbcLinkUpdater.updateStarCount(link, starCount);
        jdbcLinkUpdater.updateUpdatedDate(link, updatedAt);
    }
}
