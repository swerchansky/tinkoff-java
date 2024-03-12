package edu.java.service.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.service.LinkUpdater;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private final LinkRepository linkRepository;

    @Override
    public void updateCheckedDate(List<Link> links) {
        links.forEach(link -> linkRepository.updateCheckedDate(link.getUrl()));
    }

    @Override
    public List<Link> getOldLinks() {
        return linkRepository.findOld();
    }
}
