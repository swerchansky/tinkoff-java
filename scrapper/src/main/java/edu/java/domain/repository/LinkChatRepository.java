package edu.java.domain.repository;

import edu.java.domain.dto.LinkChat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LinkChatRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<LinkChat> linkChatRowMapper;

    public LinkChat find(URI url, Long chatId) {
        String sql = "select * from link_chat join link using (url) where url = ? and chat_id = ?";
        return jdbcOperations.queryForObject(sql, linkChatRowMapper, url, chatId);
    }

    public List<LinkChat> findAll() {
        String sql = "select * from link_chat join link using (url)";
        return jdbcOperations.query(sql, linkChatRowMapper);
    }

    public LinkChat add(URI url, Long chatId) {
        String sql = "insert into link_chat (url, chat_id) values (?, ?) on conflict do nothing";
        jdbcOperations.update(sql, url, chatId);
        return find(url, chatId);
    }

    public void remove(URI url, Long chatId) {
        String sql = "delete from link_chat where url = ? and chat_id = ?";
        jdbcOperations.update(sql, url, chatId);
    }
}
