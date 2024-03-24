package edu.java.domain.repository.jooq;

import edu.java.IntegrationEnvironment;
import edu.java.IntegrationEnvironment.IntegrationEnvironmentConfiguration;
import edu.java.configuration.db.DataBaseConfiguration;
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
    JooqLinkRepository.class
})
class JooqLinkRepositoryIntegrationTest extends IntegrationEnvironment {
    private static final URI URL = URI.create("http://yandex.ru");
    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Test
    @Transactional
    @Rollback
    @DisplayName("add link")
    public void add() {
        Link expected = jooqLinkRepository.add(URL, OffsetDateTime.now(), 0, 1);
        List<Link> actualLinks = jooqLinkRepository.findAll();

        assertThat(actualLinks).hasSize(1);
        assertThat(actualLinks).containsExactly(expected);
        Link actual = actualLinks.getFirst();
        assertThat(actual.getUrl()).isEqualTo(URL);
        assertThat(actual.getStarCount()).isEqualTo(0);
        assertThat(actual.getAnswerCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("remove link")
    public void remove() {
        jooqLinkRepository.add(URL, OffsetDateTime.now(), 0, 1);
        jooqLinkRepository.remove(URL);

        List<Link> actualLinks = jooqLinkRepository.findAll();
        assertThat(actualLinks).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find by url")
    public void findByUrl() {
        jooqLinkRepository.add(URL, OffsetDateTime.now(), 0, 1);
        Link actual = jooqLinkRepository.findByUrl(URL);

        assertThat(actual).isNotNull();
        assertThat(actual.getUrl()).isEqualTo(URL);
        assertThat(actual.getStarCount()).isEqualTo(0);
        assertThat(actual.getAnswerCount()).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("find by unknown url")
    public void findByUnknownUrl() {
        Link actual = jooqLinkRepository.findByUrl(URL);

        assertThat(actual).isNull();
    }
}
