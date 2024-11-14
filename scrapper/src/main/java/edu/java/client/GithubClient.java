package edu.java.client;

import edu.java.client.dto.GithubRepositoryResponse;
import edu.java.client.exception.ApiErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
public class GithubClient {
    private final WebClient githubWebClient;
    private final Retry retry;

    public Mono<GithubRepositoryResponse> getRepositoryInfo(String owner, String repositoryName) {
        return githubWebClient.get()
            .uri("/repos/{owner}/{repositoryName}", owner, repositoryName)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse -> Mono.error(new ApiErrorException("Github API error"))
            )
            .bodyToMono(GithubRepositoryResponse.class)
            .retryWhen(retry);
    }
}
