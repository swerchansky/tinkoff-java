package edu.java.bot.di;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import edu.java.bot.command.Command;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotBeansProvider {
    @Bean
    public TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    public BotCommand[] botCommands(List<Command> commands) {
        return commands.stream()
            .map(Command::getBotCommand)
            .toArray(BotCommand[]::new);
    }
}
