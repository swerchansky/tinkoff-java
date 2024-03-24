package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.jpa.LinkEntity;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkUpdater;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {
    private final JpaLinkRepository jpaLinkRepository;

    @Override
    @Transactional
    public void updateCheckedDate(List<Link> links) {
        links.forEach(link -> {
                LinkEntity linkEntity = jpaLinkRepository.findByUrl(link.getUrl().toString());
                linkEntity.setCheckedDate(OffsetDateTime.now());
                jpaLinkRepository.save(linkEntity);
            }
        );
    }

    @Override
    @Transactional
    public void updateUpdatedDate(Link link, OffsetDateTime updatedDate) {
        LinkEntity linkEntity = jpaLinkRepository.findByUrl(link.getUrl().toString());
        linkEntity.setUpdatedDate(updatedDate);
        jpaLinkRepository.save(linkEntity);
    }

    @Override
    @Transactional
    public void updateStarCount(Link link, Integer starCount) {
        LinkEntity linkEntity = jpaLinkRepository.findByUrl(link.getUrl().toString());
        linkEntity.setStarCount(starCount);
        jpaLinkRepository.save(linkEntity);
    }

    @Override
    @Transactional
    public void updateAnswerCount(Link link, Integer answerCount) {
        LinkEntity linkEntity = jpaLinkRepository.findByUrl(link.getUrl().toString());
        linkEntity.setAnswerCount(answerCount);
        jpaLinkRepository.save(linkEntity);
    }

    @Override
    @Transactional
    public List<Link> getOldLinks() {
        return jpaLinkRepository.findOld().stream()
            .map(linkEntity -> new Link(
                URI.create(linkEntity.getUrl()),
                linkEntity.getStarCount(),
                linkEntity.getAnswerCount(),
                linkEntity.getCheckedDate(),
                linkEntity.getUpdatedDate()
            ))
            .toList();
    }
}
