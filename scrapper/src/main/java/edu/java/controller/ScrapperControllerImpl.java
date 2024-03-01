package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScrapperControllerImpl implements ScrapperController {
    private static final LinkResponse LINK_RESPONSE_STUB = new LinkResponse(2, URI.create("https://www.google.com"));

    @Override
    public void addChat(long id) {
        log.debug("addChat stub: id={}", id);
    }

    @Override
    public void deleteChat(long id) {
        log.debug("deleteChat stub: id={}", id);
    }

    @Override
    public ListLinksResponse getLinks(long id) {
        log.debug("getLinks stub: id={}", id);
        return new ListLinksResponse(List.of(LINK_RESPONSE_STUB), 1);
    }

    @Override
    public LinkResponse addLink(long id, AddLinkRequest link) {
        log.debug("addLink stub: id={}, link={}", id, link);
        return LINK_RESPONSE_STUB;
    }

    @Override
    public LinkResponse deleteLink(long id, RemoveLinkRequest link) {
        log.debug("deleteLink stub: id={}, link={}", id, link);
        return LINK_RESPONSE_STUB;
    }
}
