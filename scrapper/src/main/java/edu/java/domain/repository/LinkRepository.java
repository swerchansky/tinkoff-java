package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkRepository {
    Link findByUrl(URI url);

    List<Link> findAll();

    List<Link> findOld();

    Link add(URI url, OffsetDateTime updatedDate, Integer starCount, Integer answerCount);

    void remove(URI url);

    void updateCheckedDate(URI url);

    void updateUpdatedDate(URI url, OffsetDateTime updatedDate);

    void updateStarCount(URI url, Integer starCount);

    void updateAnswerCount(URI url, Integer answerCount);
}
