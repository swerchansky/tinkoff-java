package edu.java.service.jooq;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqLinkUpdater implements LinkUpdater {
    private final JooqLinkRepository jdbcLinkRepository;

    @Override
    public void updateCheckedDate(List<Link> links) {
        links.forEach(link -> jdbcLinkRepository.updateCheckedDate(link.getUrl()));
    }

    @Override
    public void updateUpdatedDate(Link link, OffsetDateTime updatedDate) {
        jdbcLinkRepository.updateUpdatedDate(link.getUrl(), updatedDate);
    }

    @Override
    public void updateStarCount(Link link, Integer starCount) {
        jdbcLinkRepository.updateStarCount(link.getUrl(), starCount);
    }

    @Override
    public void updateAnswerCount(Link link, Integer answerCount) {
        jdbcLinkRepository.updateAnswerCount(link.getUrl(), answerCount);
    }

    @Override
    public List<Link> getOldLinks() {
        return jdbcLinkRepository.findOld();
    }
}
