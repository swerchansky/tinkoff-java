package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    @Override
    public String getCommandName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "start bot";
    }

    @Override
    public String execute(List<String> arguments) {
        return """
            Hello! I'm a bot that will help you track changes to websites and notify you of them

            You can see the list of available commands with the command /help
            """;
    }
}
