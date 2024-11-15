package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.LinkChatRepository;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<LinkChat> linkChatRowMapper;

    @Override
    public LinkChat find(URI url, Long chatId) {
        try {
            String sql = "select * from link_chat join link using (url) where url = ? and chat_id = ?";
            return jdbcOperations.queryForObject(sql, linkChatRowMapper, url.toString(), chatId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<LinkChat> findByUrl(URI url) {
        String sql = "select * from link_chat join link using (url) where url = ?";
        return jdbcOperations.query(sql, linkChatRowMapper, url.toString());
    }

    @Override
    public List<LinkChat> findByChatId(Long chatId) {
        String sql = "select * from link_chat join link using (url) where chat_id = ?";
        return jdbcOperations.query(sql, linkChatRowMapper, chatId);
    }

    @Override
    public List<LinkChat> findAll() {
        String sql = "select * from link_chat join link using (url)";
        return jdbcOperations.query(sql, linkChatRowMapper);
    }

    @Override
    public LinkChat add(URI url, Long chatId) {
        String sql = "insert into link_chat (url, chat_id) values (?, ?) on conflict do nothing";
        jdbcOperations.update(sql, url.toString(), chatId);
        return find(url, chatId);
    }

    @Override
    public void remove(URI url, Long chatId) {
        String sql = "delete from link_chat where url = ? and chat_id = ?";
        jdbcOperations.update(sql, url.toString(), chatId);
    }
}
