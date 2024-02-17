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
        return "This command is not implemented yet";
    }
}
