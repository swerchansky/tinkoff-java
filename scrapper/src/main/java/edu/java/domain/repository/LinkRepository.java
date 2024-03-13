package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.net.URI;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
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
        String sql = "select * from link where checked_date < now() - interval '5 minutes'";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    public Link add(URI url) {
        String sql =
            "insert into link (url, updated_date, checked_date) values (?, now(), now()) on conflict do nothing";
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

    public void updateUpdatedDate(URI url, OffsetDateTime updatedDate) {
        String sql = "update link set updated_date = ? where url = ?";
        Timestamp timestamp = Timestamp.valueOf(updatedDate.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        jdbcOperations.update(sql, timestamp, url.toString());
    }
}
