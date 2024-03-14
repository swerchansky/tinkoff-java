package edu.java.domain.repository.jooq;

import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.LinkChatRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.repository.jooq.generated.Tables.LINK;
import static edu.java.domain.repository.jooq.generated.Tables.LINK_CHAT;

@Repository
@RequiredArgsConstructor
public class JooqLinkChatRepository implements LinkChatRepository {
    private final DSLContext dslContext;

    @Override
    public LinkChat find(URI url, Long chatId) {
        try {
            return dslContext.selectFrom(LINK_CHAT.join(LINK).using(LINK.URL))
                .where(LINK_CHAT.URL.eq(url.toString()))
                .and(LINK_CHAT.CHAT_ID.eq(chatId))
                .fetchOneInto(LinkChat.class);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<LinkChat> findByUrl(URI url) {
        return dslContext.selectFrom(LINK_CHAT.join(LINK).using(LINK.URL))
            .where(LINK_CHAT.URL.eq(url.toString()))
            .fetchInto(LinkChat.class);
    }

    @Override
    public List<LinkChat> findByChatId(Long chatId) {
        return dslContext.selectFrom(LINK_CHAT.join(LINK).using(LINK.URL))
            .where(LINK_CHAT.CHAT_ID.eq(chatId))
            .fetchInto(LinkChat.class);
    }

    @Override
    public List<LinkChat> findAll() {
        return dslContext.selectFrom(LINK_CHAT.join(LINK).using(LINK.URL))
            .fetchInto(LinkChat.class);
    }

    @Override
    public LinkChat add(URI url, Long chatId) {
        dslContext.insertInto(LINK_CHAT)
            .set(LINK_CHAT.URL, url.toString())
            .set(LINK_CHAT.CHAT_ID, chatId)
            .onConflictDoNothing()
            .execute();
        return find(url, chatId);
    }

    @Override
    public void remove(URI url, Long chatId) {
        dslContext.deleteFrom(LINK_CHAT)
            .where(LINK_CHAT.URL.eq(url.toString()))
            .and(LINK_CHAT.CHAT_ID.eq(chatId))
            .execute();
    }
}
