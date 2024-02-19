package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    @Override
    public String getCommandName() {
        return "/track";
    }

    @Override
    public String getDescription() {
        return "`<link>` parameter required to start tracking a link";
    }

    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 1) {
            return """
                To start tracking a link, use the /track command followed by the link
                Example: /track https://www.google.com
                """;
        }
        return "This command is not implemented yet";
    }
}
