package edu.java.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.exception.ApiErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.codec.DecodingException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
class GithubClientTest {
    private final GithubClient githubClient = new GithubClient(WebClient.create("http://localhost:8029"));

    @Test
    @DisplayName("Should return repository info when API call is successful")
    void shouldReturnRepositoryInfoWhenApiCallIsSuccessful() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("client/githubApiSuccessfullyResponse.json")
                .withStatus(200)));

        StepVerifier.create(githubClient.getRepositoryInfo("owner", "repo"))
            .assertNext(response -> {
                assertThat(response.getOwner()).isEqualTo("owner");
                assertThat(response.getName()).isEqualTo("repo");
                assertThat(response.getUpdatedAt()).isEqualTo("2024-02-19T10:45:34Z");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Should throw ApiErrorException when API call fails")
    void shouldThrowApiErrorExceptionWhenApiCallFails() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withStatus(500)));

        StepVerifier.create(githubClient.getRepositoryInfo("owner", "repo"))
            .expectErrorSatisfies(throwable -> assertThat(throwable)
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("Github API error"))
            .verify();
    }

    @Test
    @DisplayName("Should throw DecodingException when API returns invalid JSON")
    void shouldThrowJsonProcessingExceptionWhenApiReturnsInvalidJson() {
        stubFor(get(urlEqualTo("/repos/owner/repo"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("invalid json")
                .withStatus(200)));

        StepVerifier.create(githubClient.getRepositoryInfo("owner", "repo"))
            .expectErrorMatches(throwable -> throwable instanceof DecodingException)
            .verify();
    }
}
