package edu.java.bot.command;

import java.util.List;

public interface Command {
    String getCommandName();

    String execute(List<String> arguments);

    default boolean isApplicable(String command) {
        return getCommandName().equals(command);
    }
}
