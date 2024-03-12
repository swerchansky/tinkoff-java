package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.net.URI;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LinkRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<Link> linkRowMapper;

    public Link findById(Long id) {
        String sql = "select * from link where link_id = ?";
        return jdbcOperations.queryForObject(sql, linkRowMapper, id);
    }

    public Link findByLink(URI link) {
        String sql = "select * from link where link = ?";
        return jdbcOperations.queryForObject(sql, linkRowMapper, link.toString());
    }

    public List<Link> findAll() {
        String sql = "select * from link";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    public Link add(URI link) {
        String sql = "insert into link (link, checked_date) values (?, now()) on conflict do nothing";
        jdbcOperations.update(sql, link.toString());
        return findByLink(link);
    }

    public void remove(Long id) {
        String sql = "delete from link where link_id = ?";
        jdbcOperations.update(sql, id);
    }
}
