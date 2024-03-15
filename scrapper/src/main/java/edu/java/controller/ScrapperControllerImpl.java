package edu.java.controller;

import edu.java.client.GithubClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.dto.GithubRepositoryResponse;
import edu.java.client.dto.StackOverflowQuestionsResponse;
import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.controller.exception.LastUpdateTimeUnresolvedException;
import edu.java.controller.exception.LinkNotSupportedException;
import edu.java.controller.exception.LinkNotTrackedException;
import edu.java.domain.dto.Link;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.utils.parser.LinkParser;
import edu.java.utils.parser.result.GithubLinkParserResult;
import edu.java.utils.parser.result.LinkParserResult;
import edu.java.utils.parser.result.StackOverflowlinkParserResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapperControllerImpl implements ScrapperController {
    private final LinkService jdbcLinkService;
    private final ChatService jdbcChatService;
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public void addChat(long id) {
        jdbcChatService.register(id);
    }

    @Override
    public ListLinksResponse getLinks(long id) {
        List<Link> links = jdbcLinkService.findLinks(id);
        List<LinkResponse> linkResponses = links.stream().map(link -> new LinkResponse(link.getUrl())).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse addLink(long id, AddLinkRequest link) {
        LinkParserResult linkParserResult = getLinkParserResult(link.getLink());
        URI url = getUri(linkParserResult);
        LinkInfo linkInfo = getLinkInfo(linkParserResult);
        if (linkInfo == null) {
            throw new LastUpdateTimeUnresolvedException();
        }
        jdbcLinkService.add(url, id, linkInfo.getLastUpdate(), linkInfo.getStarCount(), linkInfo.getAnswerCount());
        return new LinkResponse(url);
    }

    @Override
    public LinkResponse deleteLink(long id, RemoveLinkRequest link) {
        LinkParserResult linkParserResult = getLinkParserResult(link.getLink());
        URI url = getUri(linkParserResult);
        if (jdbcLinkService.findLinkWithId(url, id) == null) {
            throw new LinkNotTrackedException();
        }
        jdbcLinkService.remove(url, id);
        return new LinkResponse(url);
    }

    private static LinkParserResult getLinkParserResult(URI link) {
        LinkParserResult linkParserResult = LinkParser.parseLinkInfo(link);
        if (linkParserResult == null) {
            throw new LinkNotSupportedException();
        }
        return linkParserResult;
    }

    @SuppressWarnings("InnerAssignment")
    private static URI getUri(LinkParserResult linkParserResult) {
        URI url;
        switch (linkParserResult) {
            case GithubLinkParserResult githubLinkParserResult -> url = githubLinkParserResult.getUrl();
            case StackOverflowlinkParserResult stackOverflowlinkParserResult ->
                url = stackOverflowlinkParserResult.getUrl();
            default -> throw new LinkNotSupportedException();
        }
        return url;
    }

    @SuppressWarnings("InnerAssignment")
    private LinkInfo getLinkInfo(LinkParserResult linkParserResult) {
        LinkInfo linkInfo = null;
        switch (linkParserResult) {
            case GithubLinkParserResult githubLinkParserResult -> {
                GithubRepositoryResponse repositoryInfo = githubClient.getRepositoryInfo(
                    githubLinkParserResult.getOwner(),
                    githubLinkParserResult.getRepositoryName()
                ).block();
                if (repositoryInfo != null) {
                    linkInfo = new LinkInfo(
                        repositoryInfo.getUpdatedAt(),
                        repositoryInfo.getStarCount(),
                        null
                    );
                }
            }
            case StackOverflowlinkParserResult stackOverflowlinkParserResult -> {
                StackOverflowQuestionsResponse questionInfo = stackOverflowClient.getQuestionInfo(
                    stackOverflowlinkParserResult.getId()
                ).block();
                if (questionInfo != null && !questionInfo.getQuestions().isEmpty()) {
                    StackOverflowQuestionsResponse.QuestionResponse question = questionInfo.getQuestions().getFirst();
                    linkInfo = new LinkInfo(question.getUpdatedAt(), null, question.getAnswerCount());
                }
            }
            default -> log.error("Link not supported");
        }
        return linkInfo;
    }

    @Data
    @AllArgsConstructor
    private static class LinkInfo {
        private OffsetDateTime lastUpdate;
        private Integer starCount;
        private Integer answerCount;
    }
}
