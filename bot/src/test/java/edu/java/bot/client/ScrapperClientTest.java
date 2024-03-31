package edu.java.bot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.exception.ApiErrorException;
import edu.java.bot.configuration.retry.RetryConfiguration;
import java.net.URI;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@WireMockTest(httpPort = 8029)
@SpringBootTest(classes = {RetryConfiguration.class})
@ContextConfiguration(initializers = ScrapperClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ScrapperClientTest {
    private final ScrapperClient scrapperClient;

    @Autowired
    public ScrapperClientTest(Retry retry) {
        this.scrapperClient = new ScrapperClient(WebClient.create("http://localhost:8029"), retry);
    }

    @Test
    @DisplayName("register chat successfully")
    void registerChatSuccessfully() {
        stubFor(WireMock.post("/tg-chat/123")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())));

        StepVerifier.create(scrapperClient.registerChat(123L))
            .expectComplete()
            .verify();
    }

    @Test
    @DisplayName("get links successfully")
    void getLinksSuccessfully() {
        stubFor(WireMock.get("/links")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"links\":[],\"size\":0}")));

        StepVerifier.create(scrapperClient.getLinks(123L))
            .assertNext(response -> {
                    assertTrue(response.getLinks().isEmpty());
                    assertThat(response.getSize()).isEqualTo(0);
                }
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("add link successfully")
    void addLinkSuccessfully() {
        stubFor(WireMock.post("/links")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"url\":\"http://example.com\",\"id\":123}")));

        StepVerifier.create(scrapperClient.addLink(123L, URI.create("http://example.com")))
            .assertNext(response -> {
                    assertThat(response.getUrl().toString()).isEqualTo("http://example.com");
                    assertThat(response.getId()).isEqualTo(123L);
                }
            )
            .verifyComplete();
    }

    @Test
    @DisplayName("delete link successfully")
    void deleteLinkSuccessfully() {
        stubFor(WireMock.delete("/links")
            .willReturn(aResponse().withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("{\"url\":\"http://example.com\"}")));

        StepVerifier.create(scrapperClient.deleteLink(123L, URI.create("http://example.com")))
            .assertNext(response -> assertThat(response.getUrl().toString()).isEqualTo("http://example.com"))
            .verifyComplete();
    }

    @Test
    @DisplayName("Should retry when API call fails")
    void apiError() {
        stubFor(WireMock.post("/tg-chat/123")
            .willReturn(aResponse().withStatus(HttpStatus.BAD_REQUEST.value())
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(
                    "{\"description\":\"Invalid request\",\"code\":\"400\",\"exceptionMessage\":\"Invalid chat id\"}")));

        StepVerifier.create(scrapperClient.registerChat(123L))
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(ApiErrorException.class)
                    .hasMessageContaining("Invalid request")
            ).verify();
        verify(4, postRequestedFor(urlPathTemplate("/tg-chat/123")));
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
