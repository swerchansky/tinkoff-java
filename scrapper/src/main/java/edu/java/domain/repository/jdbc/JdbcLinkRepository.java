package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import static edu.java.utils.Time.getTimestamp;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcOperations jdbcOperations;
    private final RowMapper<Link> linkRowMapper;

    @Override
    public Link findByUrl(URI url) {
        try {
            String sql = "select * from link where url = ?";
            return jdbcOperations.queryForObject(sql, linkRowMapper, url.toString());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Link> findAll() {
        String sql = "select * from link";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    @Override
    public List<Link> findOld() {
        String sql = "select * from link where checked_date < now() - interval '60 seconds'";
        return jdbcOperations.query(sql, linkRowMapper);
    }

    @Override
    public Link add(URI url, OffsetDateTime updatedDate, Integer starCount, Integer answerCount) {
        String sql = """
            insert into link (url, star_count, answer_count, updated_date, checked_date)\s
            values (?, ?, ?, ?, now())\s
            on conflict do nothing
            """;
        jdbcOperations.update(sql, url.toString(), starCount, answerCount, getTimestamp(updatedDate));
        return findByUrl(url);
    }

    @Override
    public void remove(URI url) {
        String sql = "delete from link where url = ?";
        jdbcOperations.update(sql, url.toString());
    }

    @Override
    public void updateCheckedDate(URI url) {
        String sql = "update link set checked_date = now() where url = ?";
        jdbcOperations.update(sql, url.toString());
    }

    @Override
    public void updateUpdatedDate(URI url, OffsetDateTime updatedDate) {
        String sql = "update link set updated_date = ? where url = ?";
        jdbcOperations.update(sql, getTimestamp(updatedDate), url.toString());
    }

    @Override
    public void updateStarCount(URI url, Integer starCount) {
        String sql = "update link set star_count = ? where url = ?";
        jdbcOperations.update(sql, starCount, url.toString());
    }

    @Override
    public void updateAnswerCount(URI url, Integer answerCount) {
        String sql = "update link set answer_count = ? where url = ?";
        jdbcOperations.update(sql, answerCount, url.toString());
    }
}
