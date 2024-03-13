package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String getCommandName() {
        return "/track";
    }

    @Override
    public String getDescription() {
        return "`<link>` parameter required to start tracking a link";
    }

    @Override
    public Mono<String> execute(Long chatId, List<String> arguments) {
        if (arguments.size() != 1) {
            return Mono.just("""
                To start tracking a link, use the /track command followed by the link
                Example: /track https://www.google.com
                """);
        }
        return scrapperClient.addLink(chatId, arguments.getFirst())
            .map(linkResponse -> "Link " + linkResponse.url + " successfully added for tracking")
            .onErrorResume(error -> Mono.just(error.getMessage()));
    }
}
