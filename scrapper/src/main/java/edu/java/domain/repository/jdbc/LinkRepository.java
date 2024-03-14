package edu.java.domain.repository.jdbc;

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
        String sql = "select * from link where checked_date < now() - interval '60 seconds'";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    public Link add(URI url, OffsetDateTime updatedDate, Integer starCount, Integer answerCount) {
        String sql = """
            insert into link (url, star_count, answer_count, updated_date, checked_date)\s
            values (?, ?, ?, ?, now())\s
            on conflict do nothing
            """;
        jdbcOperations.update(sql, url.toString(), starCount, answerCount, getTimestamp(updatedDate));
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
        jdbcOperations.update(sql, getTimestamp(updatedDate), url.toString());
    }

    private static Timestamp getTimestamp(OffsetDateTime updatedDate) {
        return Timestamp.valueOf(updatedDate.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }

    public void updateStarCount(URI url, Integer starCount) {
        String sql = "update link set star_count = ? where url = ?";
        jdbcOperations.update(sql, starCount, url.toString());
    }

    public void updateAnswerCount(URI url, Integer answerCount) {
        String sql = "update link set answer_count = ? where url = ?";
        jdbcOperations.update(sql, answerCount, url.toString());
    }
}
