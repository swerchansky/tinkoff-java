package edu.java.domain.repository;

import edu.java.domain.dto.LinkChat;
import java.net.URI;
import java.util.List;

public interface LinkChatRepository {
    LinkChat find(URI url, Long chatId);

    List<LinkChat> findByUrl(URI url);

    List<LinkChat> findByChatId(Long chatId);

    List<LinkChat> findAll();

    LinkChat add(URI url, Long chatId);

    void remove(URI url, Long chatId);
}
