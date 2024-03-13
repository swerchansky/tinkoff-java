package edu.java.service.jdbc;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.DataBaseConfiguration;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationEnvironmentConfiguration.class,
    DataBaseConfiguration.class,
    LinkRepository.class,
    JdbcLinkUpdater.class
})
class JdbcLinkUpdaterIntegrationTest extends IntegrationEnvironment {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private JdbcLinkUpdater linkUpdater;

    @Test
    @Transactional
    @Rollback
    @DisplayName("update checked date")
    void updateCheckedDate() {
        Link link = linkRepository.add(URI.create("https://google.com"));

        linkUpdater.updateCheckedDate(List.of(link));

        assertThat(linkRepository.findAll().getFirst().getCheckedDate()).isAfterOrEqualTo(link.getCheckedDate());
    }
}
