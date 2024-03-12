package edu.java.service.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkChatRepository linkChatRepository;
    private final LinkRepository linkRepository;

    @Override
    public Link add(URI url, Long chatId) {
        Link link = linkRepository.add(url);
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
    public List<Chat> findChats(URI url) {
        List<LinkChat> linkChats = linkChatRepository.findByUrl(url);
        return linkChats.stream().map(LinkChat::getChat).toList();
    }

    @Override
    public List<Link> findLinks(Long chatId) {
        List<LinkChat> linkChats = linkChatRepository.findByChatId(chatId);
        return linkChats.stream().map(LinkChat::getLink).toList();
    }
}