package edu.java.bot.runner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final List<Command> commands;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::processCommand);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processCommand(Update update) {
    }
}
