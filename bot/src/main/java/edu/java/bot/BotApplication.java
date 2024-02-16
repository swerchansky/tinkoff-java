package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.runner.BotRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BotApplication.class, args);
        context.getBean(BotRunner.class).startBot();
    }
}
