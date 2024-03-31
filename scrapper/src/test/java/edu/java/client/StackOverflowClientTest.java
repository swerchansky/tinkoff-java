package edu.java.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.dto.StackOverflowQuestionsResponse.QuestionResponse;
import edu.java.client.exception.ApiErrorException;
import edu.java.configuration.retry.RetryConfiguration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.codec.DecodingException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
@SpringBootTest(classes = {RetryConfiguration.class})
@ContextConfiguration(initializers = StackOverflowClientTest.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StackOverflowClientTest {
    private final StackOverflowClient stackOverflowClient;

    @Autowired
    public StackOverflowClientTest(Retry retry) {
        this.stackOverflowClient = new StackOverflowClient(WebClient.create("http://localhost:8029"), retry);
    }

    @Test
    @DisplayName("Should return question info when API call is successful")
    void shouldReturnRepositoryInfoWhenApiCallIsSuccessful() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBodyFile("client/stackOverflowApiSuccessfullyResponse.json")
                .withStatus(200)));

        StepVerifier.create(stackOverflowClient.getQuestionInfo(1L))
            .assertNext(response -> {
                assertThat(response.getQuestions()).hasSize(1);
                QuestionResponse question = response.questions.getFirst();
                assertThat(question.getOwner()).isEqualTo("owner");
                assertThat(question.getTitle()).isEqualTo("Binary Data in MySQL");
                assertThat(question.getUpdatedAt()).isEqualTo("2020-12-03T03:37:51Z");
            })
            .verifyComplete();
    }

    @Test
    @DisplayName("Should retry when API call fails")
    void shouldThrowApiErrorExceptionWhenApiCallFails() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withStatus(500)));

        StepVerifier.create(stackOverflowClient.getQuestionInfo(1L))
            .expectErrorSatisfies(throwable -> assertThat(throwable)
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("Stackoverflow API error"))
            .verify();
        verify(4, getRequestedFor(urlEqualTo("/questions/1?site=stackoverflow")));
    }

    @Test
    @DisplayName("Should throw DecodingException when API returns invalid JSON")
    void shouldThrowJsonProcessingExceptionWhenApiReturnsInvalidJson() {
        stubFor(get(urlEqualTo("/questions/1?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("invalid json")
                .withStatus(200)));

        StepVerifier.create(stackOverflowClient.getQuestionInfo(1L))
            .expectErrorMatches(throwable -> throwable instanceof DecodingException)
            .verify();
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
