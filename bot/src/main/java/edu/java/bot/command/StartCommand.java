package edu.java.bot.command;

import edu.java.bot.client.ScrapperClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ScrapperClient scrapperClient;

    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "start bot";
    }

    @Override
    public Mono<String> execute(Long chatId, List<String> arguments) {
        return scrapperClient.registerChat(chatId).then(Mono.fromCallable(
            () -> """
                Hello! I'm a bot that will help you track changes to websites and notify you of them

                You can see the list of available commands with the command /help
                """
        )).onErrorResume(error -> Mono.just(error.getMessage()));
    }
}
