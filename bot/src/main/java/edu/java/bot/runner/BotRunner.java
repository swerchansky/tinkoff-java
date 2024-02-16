package edu.java.bot.runner;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotRunner {
    private final TelegramBot telegramBot;
    private final BotUpdatesListener botUpdatesListener;

    public void startBot() {
        telegramBot.setUpdatesListener(botUpdatesListener);
    }

    @PreDestroy
    public void stopBot() {
        telegramBot.removeGetUpdatesListener();
    }
}
