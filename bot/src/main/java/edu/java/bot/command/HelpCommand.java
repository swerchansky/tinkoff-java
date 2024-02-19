package edu.java.bot.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public String execute(List<String> arguments) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("*Available commands:*").append(System.lineSeparator());
        commands.forEach(command ->
            helpMessage.append(command.getCommandName())
                .append(" - ")
                .append(command.getDescription())
                .append(System.lineSeparator())
        );
        return helpMessage.toString();
    }
}
