package edu.java.service.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.dto.jpa.ChatEntity;
import edu.java.domain.dto.jpa.LinkEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkService;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaChatRepository jpaChatRepository;
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    @Transactional
    public Link add(URI url, Long chatId, OffsetDateTime lastUpdate, Integer starCount, Integer answerCount) {
        ChatEntity chat = jpaChatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            chat = new ChatEntity();
            chat.setChatId(chatId);
            jpaChatRepository.save(chat);
        }

        LinkEntity link;
        if (jpaLinkRepository.existsById(url.toString())) {
            link = jpaLinkRepository.findById(url.toString()).get();
        } else {
            link = new LinkEntity();
            link.setUrl(url.toString());
            link.setAnswerCount(answerCount);
            link.setStarCount(starCount);
            link.setUpdatedDate(lastUpdate);
            link.setCheckedDate(OffsetDateTime.now());
        }
        link.getChats().add(chat);
        jpaLinkRepository.save(link);

        chat.getLinks().add(link);
        jpaChatRepository.save(chat);

        return new Link(
            URI.create(link.getUrl()),
            link.getStarCount(),
            link.getAnswerCount(),
            link.getUpdatedDate(),
            link.getCheckedDate()
        );
    }

    @Override
    @Transactional
    public void remove(URI url, Long chatId) {
        ChatEntity chat = jpaChatRepository.findById(chatId).orElse(null);
        LinkEntity link = jpaLinkRepository.findById(url.toString()).orElse(null);

        if (chat == null || link == null) {
            return;
        }

        chat.getLinks().remove(link);
        jpaChatRepository.save(chat);

        link.getChats().remove(chat);
        jpaLinkRepository.save(link);

        if (link.getChats().isEmpty()) {
            jpaLinkRepository.delete(link);
        }
    }

    @Override
    @Transactional
    public Link findLink(URI url) {
        LinkEntity link = jpaLinkRepository.findById(url.toString()).orElse(null);
        if (link == null) {
            return null;
        }

        return new Link(
            URI.create(link.getUrl()),
            link.getStarCount(),
            link.getAnswerCount(),
            link.getUpdatedDate(),
            link.getCheckedDate()
        );
    }

    @Override
    @Transactional
    public LinkChat findLinkWithId(URI url, Long chatId) {
        ChatEntity chat = jpaChatRepository.findById(chatId).orElse(null);
        LinkEntity link = jpaLinkRepository.findById(url.toString()).orElse(null);

        if (chat == null || link == null) {
            return null;
        }

        return new LinkChat(
            URI.create(link.getUrl()),
            chat.getChatId(),
            link.getStarCount(),
            link.getAnswerCount(),
            link.getUpdatedDate(),
            link.getCheckedDate()
        );
    }

    @Override
    @Transactional
    public List<Chat> findChats(URI url) {
        LinkEntity link = jpaLinkRepository.findById(url.toString()).orElse(null);
        if (link == null) {
            return null;
        }

        return link.getChats().stream().map(chat -> new Chat(chat.getChatId())).toList();
    }

    @Override
    @Transactional
    public List<Link> findLinks(Long chatId) {
        ChatEntity chat = jpaChatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return null;
        }

        return chat.getLinks().stream().map(link -> new Link(
            URI.create(link.getUrl()),
            link.getStarCount(),
            link.getAnswerCount(),
            link.getUpdatedDate(),
            link.getCheckedDate()
        )).toList();
    }
}
