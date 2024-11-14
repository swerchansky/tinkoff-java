package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import static java.lang.System.lineSeparator;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String getCommandName() {
        return "/list";
    }

    @Override
    public String getDescription() {
        return "show list of tracked links";
    }

    @Override
    public Mono<String> execute(Long chatId, List<String> arguments) {
        return scrapperClient.getLinks(chatId).map(response ->
            response.getLinks().stream().map(link -> link.getUrl().toString()).reduce((a, b) -> a + lineSeparator() + b)
                .orElse("No links are being tracked")
        ).onErrorResume(error -> Mono.just(error.getMessage()));
    }
}
