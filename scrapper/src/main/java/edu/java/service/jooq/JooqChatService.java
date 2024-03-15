package edu.java.service.jooq;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;

    @Override
    public void register(Long chatId) {
        chatRepository.add(chatId);
    }
}
