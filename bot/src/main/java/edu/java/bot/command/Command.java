package edu.java.bot.command;

public interface Command {
    String getCommandName();

    String execute(String message);

    default boolean isApplicable(String command) {
        return getCommandName().equals(command);
    }
}
