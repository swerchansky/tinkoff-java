package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Override
    public String getCommandName() {
        return "/untrack";
    }

    @Override
    public String getDescription() {
        return "`<link>` parameter required to stop tracking a link";
    }

    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 1) {
            return """
                To stop tracking a link, use the /untrack command followed by the link
                Example: /untrack https://www.google.com
                """;
        }
        return "This command is not implemented yet";
    }
}
