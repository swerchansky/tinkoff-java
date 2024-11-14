package edu.java.service.jdbc;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Override
    public void register(Long chatId) {
        chatRepository.add(chatId);
    }
}
