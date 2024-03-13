package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<Link> linkRowMapper;

    public Link findByUrl(URI url) {
        try {
            String sql = "select * from link where url = ?";
            return jdbcOperations.queryForObject(sql, linkRowMapper, url.toString());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Link> findAll() {
        String sql = "select * from link";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    public List<Link> findOld() {
        String sql = "select * from link where checked_date < now() - interval '10 seconds'";
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

    public void updateCheckedDate(URI url) {
        String sql = "update link set checked_date = now() where url = ?";
        jdbcOperations.update(sql, url.toString());
    }
}
