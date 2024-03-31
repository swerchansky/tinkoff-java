package edu.java.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.exception.ApiErrorException;
import edu.java.configuration.retry.RetryConfiguration;
import java.net.URI;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@WireMockTest(httpPort = 8029)
@SpringBootTest(classes = {RetryConfiguration.class})
@ContextConfiguration(initializers = BotClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BotClientTest {
    private final LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
        URI.create("https://www.google.com"),
        "",
        List.of()
    );
    private final BotClient botClient;

    @Autowired
    public BotClientTest(Retry retry) {
        this.botClient = new BotClient(WebClient.create("http://localhost:8029"), retry);
    }

    @Test
    @DisplayName("update link successfully")
    void updateLinkSuccessfully() {
        stubFor(WireMock.post("/update")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("Should retry when API call fails")
    void updateLinkApiError() {
        stubFor(WireMock.post("/update")
            .willReturn(aResponse().withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"description\":\"Invalid request\",\"code\":\"400\",\"exceptionMessage\":\"Invalid link\"}")));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(ApiErrorException.class)
                    .hasMessageContaining("Invalid request")
            ).verify();
        verify(4, postRequestedFor(urlPathTemplate("/update")));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("app.retry.backoffPolicy=fixed").applyTo(applicationContext);
            TestPropertyValues.of("app.retry.attempts=3").applyTo(applicationContext);
            TestPropertyValues.of("app.retry.delay=100").applyTo(applicationContext);
            TestPropertyValues.of("app.retry.jitter=0.5").applyTo(applicationContext);
            TestPropertyValues.of("app.retry.codes=400, 406, 500").applyTo(applicationContext);
        }
    }
}
