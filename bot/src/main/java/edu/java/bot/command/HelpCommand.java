package edu.java.bot.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private final List<Command> commands;

    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "show help";
    }

    @Override
    public Mono<String> execute(Long chatId, List<String> arguments) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("*Available commands:*").append(System.lineSeparator());
        commands.forEach(command ->
            helpMessage.append(command.getCommandName())
                .append(" - ")
                .append(command.getDescription())
                .append(System.lineSeparator())
        );
        return Mono.just(helpMessage.toString());
    }
}
