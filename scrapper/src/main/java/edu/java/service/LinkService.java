package edu.java.service;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Link add(URI url, Long chatId, OffsetDateTime lastUpdate);

    void remove(URI url, Long chatId);

    Link findLink(URI url);

    LinkChat findLinkWithId(URI url, Long chatId);

    List<Chat> findChats(URI url);

    List<Link> findLinks(Long chatId);
}
