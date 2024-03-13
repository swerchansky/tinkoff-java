package edu.java.service;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    Link add(URI url, Long chatId);

    void remove(URI url, Long chatId);

    Link findLink(URI url);

    List<Chat> findChats(URI url);

    List<Link> findLinks(Long chatId);
}
