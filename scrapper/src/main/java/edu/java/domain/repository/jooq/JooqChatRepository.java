package edu.java.domain.repository.jooq;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.repository.jooq.generated.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override
    public Chat findById(Long chatId) {
        return dslContext.selectFrom(CHAT)
            .where(CHAT.CHAT_ID.eq(chatId))
            .fetchOneInto(Chat.class);
    }

    @Override
    public List<Chat> findAll() {
        return dslContext.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public Chat add(Long chatId) {
        return dslContext.insertInto(CHAT)
            .set(CHAT.CHAT_ID, chatId)
            .returning()
            .fetchOneInto(Chat.class);
    }

    @Override
    public void remove(Long chatId) {
        dslContext.deleteFrom(CHAT)
            .where(CHAT.CHAT_ID.eq(chatId))
            .execute();
    }
}
