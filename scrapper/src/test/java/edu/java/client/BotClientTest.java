package edu.java.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.exception.ApiErrorException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
class BotClientTest {
    private final BotClient botClient = new BotClient(WebClient.create("http://localhost:8029"));
    private final LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
        URI.create("https://www.google.com"),
        "",
        List.of()
    );

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
    @DisplayName("Should throw ApiErrorException when API call fails")
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
                    .hasMessageContaining("400")
                    .hasMessageContaining("Invalid link")
            ).verify();
    }
}
