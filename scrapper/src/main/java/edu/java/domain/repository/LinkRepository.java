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

    public Link findByUrl(URI url) {
        String sql = "select * from link where url = ?";
        return jdbcOperations.queryForObject(sql, linkRowMapper, url.toString());
    }

    public List<Link> findAll() {
        String sql = "select * from link";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    public Link add(URI url) {
        String sql = "insert into link (url, checked_date) values (?, now()) on conflict do nothing";
        jdbcOperations.update(sql, url.toString());
        return findByUrl(url);
    }

    public void remove(URI url) {
        String sql = "delete from link where url = ?";
        jdbcOperations.update(sql, url.toString());
    }
}
