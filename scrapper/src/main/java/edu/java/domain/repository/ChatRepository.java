package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface ChatRepository {
    Chat findById(Long chatId);

    List<Chat> findAll();

    Chat add(Long chatId);

    void remove(Long chatId);
}
