package edu.java.domain.repository.jooq;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;
import static edu.java.domain.repository.jooq.generated.Tables.LINK;
import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.localDateTimeSub;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    public Link findByUrl(URI url) {
        try {
            return dslContext.selectFrom(LINK)
                .where(LINK.URL.eq(url.toString()))
                .fetchOneInto(Link.class);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(LINK)
            .fetchInto(Link.class);
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public List<Link> findOld() {
        return dslContext.selectFrom(LINK)
            .where(LINK.CHECKED_DATE.lessThan(localDateTimeSub(currentLocalDateTime(), 60, DatePart.SECOND)))
            .fetchInto(Link.class);
    }

    @Override
    public Link add(URI url, OffsetDateTime updatedDate, Integer starCount, Integer answerCount) {
        dslContext.insertInto(LINK)
            .set(LINK.URL, url.toString())
            .set(LINK.STAR_COUNT, starCount)
            .set(LINK.ANSWER_COUNT, answerCount)
            .set(LINK.UPDATED_DATE, updatedDate.toLocalDateTime())
            .set(LINK.CHECKED_DATE, currentLocalDateTime())
            .onConflictDoNothing()
            .execute();
        return findByUrl(url);
    }

    @Override
    public void remove(URI url) {
        dslContext.deleteFrom(LINK)
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public void updateCheckedDate(URI url) {
        dslContext.update(LINK)
            .set(LINK.CHECKED_DATE, currentLocalDateTime())
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public void updateUpdatedDate(URI url, OffsetDateTime updatedDate) {
        dslContext.update(LINK)
            .set(LINK.UPDATED_DATE, updatedDate.toLocalDateTime())
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public void updateStarCount(URI url, Integer starCount) {
        dslContext.update(LINK)
            .set(LINK.STAR_COUNT, starCount)
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }

    @Override
    public void updateAnswerCount(URI url, Integer answerCount) {
        dslContext.update(LINK)
            .set(LINK.ANSWER_COUNT, answerCount)
            .where(LINK.URL.eq(url.toString()))
            .execute();
    }
}
