package edu.java.bot.command;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    @Override
    public String getCommandName() {
        return "/help";
    }

    @Override
    public String execute(List<String> arguments) {
        return """
            *Available commands:*
            /help - show help
            /start - start the bot
            /track `<link>` - track the link
            /untrack `<link>` - untrack the link
            /list - show all tracked links

            *Example:*
            /track https://www.google.com
            /untrack https://www.google.com

            *Note*: the bot can track only links that are sent with command /track in the same message
            """.stripIndent();
    }
}
