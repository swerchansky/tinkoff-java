package edu.java.service;

import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkUpdater {
    void updateCheckedDate(List<Link> links);

    List<Link> getOldLinks();
}
