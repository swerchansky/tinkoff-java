package edu.java.domain.repository;

import edu.java.domain.dto.LinkChat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LinkChatRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<LinkChat> linkChatRowMapper;

    public LinkChat findById(Long linkId, Long chatId) {
        String sql = "select * from link_chat join link using (link_id) where link_id = ? and chat_id = ?";
        return jdbcOperations.queryForObject(sql, linkChatRowMapper, linkId, chatId);
    }

    public List<LinkChat> findAll() {
        String sql = "select * from link_chat join link using (link_id)";
        return jdbcOperations.query(sql, linkChatRowMapper);
    }

    public LinkChat add(Long linkId, Long chatId) {
        String sql = "insert into link_chat (link_id, chat_id) values (?, ?) on conflict do nothing";
        jdbcOperations.update(sql, linkId, chatId);
        return findById(linkId, chatId);
    }

    public void remove(Long linkId, Long chatId) {
        String sql = "delete from link_chat where link_id = ? and chat_id = ?";
        jdbcOperations.update(sql, linkId, chatId);
    }
}
