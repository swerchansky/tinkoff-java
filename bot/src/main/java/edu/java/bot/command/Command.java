package edu.java.bot.command;

import com.pengrad.telegrambot.model.BotCommand;
import java.util.List;

public interface Command {
    String getCommandName();

    String getDescription();

    String execute(List<String> arguments);

    default boolean isApplicable(String command) {
        return getCommandName().equals(command);
    }

    default BotCommand getBotCommand() {
        return new BotCommand(getCommandName(), getDescription());
    }
}
