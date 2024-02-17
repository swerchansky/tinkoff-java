package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    @Override
    public String getCommandName() {
        return "/list";
    }

    @Override
    public String getDescription() {
        return "show list of tracked links";
    }

    @Override
    public String execute(List<String> arguments) {
        return "List of tracked links is empty";
    }
}
