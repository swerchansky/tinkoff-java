package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<Chat> chatRowMapper;

    public Chat findById(Long chatId) {
        try {
            String sql = "select chat_id from chat where chat_id = ?";
            return jdbcOperations.queryForObject(sql, chatRowMapper, chatId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Chat> findAll() {
        String sql = "select chat_id from chat";
        return jdbcOperations.query(sql, chatRowMapper);
    }

    public Chat add(Long chatId) {
        String sql = "insert into chat (chat_id) values (?) on conflict do nothing";
        jdbcOperations.update(sql, chatId);
        return findById(chatId);
    }

    public void remove(Long chatId) {
        String sql = "delete from chat where chat_id = ?";
        jdbcOperations.update(sql, chatId);
    }
}
