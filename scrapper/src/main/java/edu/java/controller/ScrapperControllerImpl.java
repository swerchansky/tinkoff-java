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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapperControllerImpl implements ScrapperController {
    private final LinkService linkService;
    private final ChatService chatService;
    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;

    @Override
    public void addChat(long id) {
        chatService.register(id);
    }

    @Override
    public ListLinksResponse getLinks(long id) {
        List<Link> links = linkService.findLinks(id);
        List<LinkResponse> linkResponses = links.stream().map(link -> new LinkResponse(link.getUrl())).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse addLink(long id, AddLinkRequest link) {
        LinkParserResult linkParserResult = getLinkParserResult(link.getLink());
        URI url = getUri(linkParserResult);
        OffsetDateTime lastUpdate = getLastUpdateTime(linkParserResult);
        if (lastUpdate == null) {
            throw new LastUpdateTimeUnresolvedException();
        }
        linkService.add(url, id, lastUpdate);
        return new LinkResponse(url);
    }

    @Override
    public LinkResponse deleteLink(long id, RemoveLinkRequest link) {
        LinkParserResult linkParserResult = getLinkParserResult(link.getLink());
        URI url = getUri(linkParserResult);
        if (linkService.findLinkWithId(url, id) == null) {
            throw new LinkNotTrackedException();
        }
        linkService.remove(url, id);
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
    private OffsetDateTime getLastUpdateTime(LinkParserResult linkParserResult) {
        OffsetDateTime lastUpdate;
        switch (linkParserResult) {
            case GithubLinkParserResult githubLinkParserResult -> {
                GithubRepositoryResponse repositoryInfo = githubClient.getRepositoryInfo(
                    githubLinkParserResult.getOwner(),
                    githubLinkParserResult.getRepositoryName()
                ).block();
                lastUpdate = repositoryInfo != null ? repositoryInfo.getUpdatedAt() : null;
            }
            case StackOverflowlinkParserResult stackOverflowlinkParserResult -> {
                StackOverflowQuestionsResponse questionInfo = stackOverflowClient.getQuestionInfo(
                    stackOverflowlinkParserResult.getId()
                ).block();
                lastUpdate = questionInfo != null ? questionInfo.getQuestions().getLast().getUpdatedAt() : null;
            }
            default -> lastUpdate = null;
        }
        return lastUpdate;
    }
}
