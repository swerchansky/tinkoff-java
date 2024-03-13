package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String getCommandName() {
        return "/untrack";
    }

    @Override
    public String getDescription() {
        return "`<link>` parameter required to stop tracking a link";
    }

    @Override
    public Mono<String> execute(Long chatId, List<String> arguments) {
        if (arguments.size() != 1) {
            return Mono.just("""
                To stop tracking a link, use the /untrack command followed by the link
                Example: /untrack https://www.google.com
                """);
        }
        return scrapperClient.deleteLink(chatId, arguments.getFirst())
            .map(linkResponse -> "Link " + linkResponse.url + " successfully removed from tracking")
            .onErrorResume(error -> Mono.just(error.getMessage()));
    }
}
