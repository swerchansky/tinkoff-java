package edu.java.domain.repository;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationEnvironmentConfiguration.class,
    DataBaseConfiguration.class,
    LinkRepository.class
})
class LinkRepositoryIntegrationTest extends IntegrationEnvironment {
    private static final URI URL = URI.create("http://google.com");
    @Autowired
    private LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link")
    public void add() {
        Link expected = linkRepository.add(URL, OffsetDateTime.now());
        List<Link> actualLinks = linkRepository.findAll();

        assertThat(actualLinks).hasSize(1);
        assertThat(actualLinks).containsExactly(expected);
        Link actual = actualLinks.getFirst();
        assertThat(actual.getUrl()).isEqualTo(URL);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link")
    public void remove() {
        linkRepository.add(URL, OffsetDateTime.now());
        linkRepository.remove(URL);

        List<Link> actualLinks = linkRepository.findAll();
        assertThat(actualLinks).isEmpty();
    }
}
