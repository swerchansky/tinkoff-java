package edu.java.bot.runner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotRunner {
    private final TelegramBot telegramBot;
    private final BotUpdatesListener botUpdatesListener;
    private final BotCommand[] botCommands;

    public void startBot() {
        telegramBot.execute(new SetMyCommands(botCommands));
        telegramBot.setUpdatesListener(botUpdatesListener);
    }

    @PreDestroy
    public void stopBot() {
        telegramBot.removeGetUpdatesListener();
    }
}
