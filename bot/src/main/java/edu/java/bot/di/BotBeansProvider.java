package edu.java.bot.di;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotBeansProvider {
    @Bean
    public TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }
}
