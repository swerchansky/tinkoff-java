package edu.java.service;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void updateCheckedDate(List<Link> links);

    void updateUpdatedDate(Link link, OffsetDateTime updatedDate);

    List<Link> getOldLinks();
}
