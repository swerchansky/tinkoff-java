package edu.java.client;

import edu.java.client.dto.LinkUpdateRequest;
import edu.java.client.exception.ApiErrorException;
import edu.java.controller.dto.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BotClient {
    private final WebClient botWebClient;

    public Mono<Void> update(LinkUpdateRequest linkUpdateRequest) {
        return botWebClient.post()
            .uri("/update")
            .bodyValue(linkUpdateRequest)
            .retrieve()
            .onStatus(this::isApiError, this::handleApiError)
            .bodyToMono(Void.class);
    }

    private Mono<ApiErrorException> handleApiError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .map(apiErrorResponse ->
                new ApiErrorException(
                    apiErrorResponse.description + ": " + apiErrorResponse.exceptionMessage,
                    Integer.parseInt(apiErrorResponse.code)
                )
            );
    }

    private boolean isApiError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
