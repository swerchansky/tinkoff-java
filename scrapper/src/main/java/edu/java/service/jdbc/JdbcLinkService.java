package edu.java.service.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkChatRepository linkChatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatRepository chatRepository;

    @Override
    public Link add(URI url, Long chatId, OffsetDateTime lastUpdate, Integer starCount, Integer answerCount) {
        Chat chat = chatRepository.findById(chatId);
        if (chat == null) {
            chatRepository.add(chatId);
        }
        Link link = linkRepository.add(url, lastUpdate, starCount, answerCount);
        linkChatRepository.add(url, chatId);
        return link;
    }

    @Override
    public void remove(URI url, Long chatId) {
        linkChatRepository.remove(url, chatId);
        if (findChats(url).isEmpty()) {
            linkRepository.remove(url);
        }
    }

    @Override
    public Link findLink(URI url) {
        return linkRepository.findByUrl(url);
    }

    @Override
    public LinkChat findLinkWithId(URI url, Long chatId) {
        return linkChatRepository.find(url, chatId);
    }

    @Override
    public List<Chat> findChats(URI url) {
        List<LinkChat> linkChats = linkChatRepository.findByUrl(url);
        return linkChats.stream().map(linkChat -> chatRepository.findById(linkChat.getChatId())).toList();
    }

    @Override
    public List<Link> findLinks(Long chatId) {
        List<LinkChat> linkChats = linkChatRepository.findByChatId(chatId);
        return linkChats.stream().map(linkChat -> linkRepository.findByUrl(linkChat.getUrl())).toList();
    }
}
