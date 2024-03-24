package edu.java.service.jpa;

import edu.java.domain.dto.jpa.ChatEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.service.ChatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository jpaChatRepository;

    @Override
    @Transactional
    public void register(Long chatId) {
        if (!jpaChatRepository.existsById(chatId)) {
            ChatEntity chatEntity = new ChatEntity();
            chatEntity.setChatId(chatId);
            jpaChatRepository.save(chatEntity);
        }
    }
}
