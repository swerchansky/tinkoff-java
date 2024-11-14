package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.BotUpdatesListener;
import edu.java.bot.command.Command;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {
    private final TelegramBot telegramBot;
    private final BotCommand[] botCommands;
    private final BotUpdatesListener botUpdatesListener;

    @PostConstruct
    public void startBot() {
        telegramBot.execute(new SetMyCommands(botCommands));
        telegramBot.setUpdatesListener(botUpdatesListener);
    }

    @PreDestroy
    public void stopBot() {
        telegramBot.removeGetUpdatesListener();
    }

    @Bean
    public static TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public static BotCommand[] botCommands(List<Command> commands) {
        return commands.stream()
            .map(Command::getBotCommand)
            .toArray(BotCommand[]::new);
    }

    @Bean
    public static Counter counter(MeterRegistry meterRegistry) {
        return meterRegistry.counter("processed_messages_count");
    }
}
