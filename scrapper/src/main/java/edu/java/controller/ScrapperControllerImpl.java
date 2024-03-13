package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import edu.java.domain.dto.Link;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.utils.parser.LinkParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScrapperControllerImpl implements ScrapperController {
    private final LinkService linkService;
    private final ChatService chatService;
    private static final LinkResponse LINK_RESPONSE_STUB = new LinkResponse(URI.create("https://www.google.com"));

    @Override
    public void addChat(long id) {
        chatService.register(id);
    }

    @Override
    public void deleteChat(long id) {
        //stub
    }

    @Override
    public ListLinksResponse getLinks(long id) {
        List<Link> links = linkService.findLinks(id);
        List<LinkResponse> linkResponses = links.stream().map(link -> new LinkResponse(link.getUrl())).toList();
        return new ListLinksResponse(linkResponses, linkResponses.size());
    }

    @Override
    public LinkResponse addLink(long id, AddLinkRequest link) {
        URI url = link.getLink();
        if (LinkParser.parseLinkInfo(url) == null) {
            throw new IllegalArgumentException("Invalid link");
        }
        linkService.add(url, id);
        return new LinkResponse(url);
    }

    @Override
    public LinkResponse deleteLink(long id, RemoveLinkRequest link) {
        linkService.remove(link.link, id);
        return new LinkResponse(link.link);
    }
}
