package edu.java.bot.client;

import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import edu.java.bot.client.exception.ApiErrorException;
import edu.java.bot.controller.dto.ApiErrorResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScrapperClient {
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final String CHAT_ENDPOINT = "/tg-chat/{id}";
    private static final String LINKS_ENDPOINT = "/links";
    private final WebClient scrapperWebClient;

    public Mono<Void> registerChat(long id) {
        return scrapperWebClient.post()
            .uri(CHAT_ENDPOINT, id)
            .retrieve()
            .onStatus(this::isApiError, this::handleApiError)
            .bodyToMono(Void.class);
    }

    public Mono<ListLinksResponse> getLinks(long id) {
        return scrapperWebClient.get()
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(this::isApiError, this::handleApiError)
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(long id, URI url) {
        return sendLinkRequestWithBody(HttpMethod.POST, id, new AddLinkRequest(url));
    }

    public Mono<LinkResponse> deleteLink(long id, URI url) {
        return sendLinkRequestWithBody(HttpMethod.DELETE, id, new RemoveLinkRequest(url));
    }

    private Mono<LinkResponse> sendLinkRequestWithBody(HttpMethod method, long id, Object requestBody) {
        return scrapperWebClient.method(method)
            .uri(LINKS_ENDPOINT)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(this::isApiError, this::handleApiError)
            .bodyToMono(LinkResponse.class);
    }

    private Mono<ApiErrorException> handleApiError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .map(apiErrorResponse -> new ApiErrorException(apiErrorResponse.description));
    }

    private boolean isApiError(HttpStatusCode code) {
        return code.is4xxClientError() || code.is5xxServerError();
    }
}
