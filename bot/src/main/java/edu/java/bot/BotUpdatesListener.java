package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import io.micrometer.core.instrument.Counter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final List<Command> commands;
    private final Counter metricsCounter;

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::processCommand);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processCommand(Update update) {
        Message message = update.message();
        if (message == null) {
            return;
        }
        log.debug("Received message: \"{}\" from user \"{}\"", message.text(), message.from().username());
        Long chatId = message.chat().id();
        CommandArguments commandArguments = CommandArguments.fromString(message.text());
        commands.stream()
            .filter(command -> command.isApplicable(commandArguments.commandName))
            .findFirst()
            .ifPresentOrElse(
                command -> command.execute(chatId, commandArguments.arguments).subscribe(
                    responseText -> {
                        telegramBot.execute(createMessage(chatId, responseText));
                        metricsCounter.increment();
                    },
                    error -> log.error("Error while executing command", error)
                ),
                () -> {
                    telegramBot.execute(new SendMessage(chatId, "Unknown command"));
                    metricsCounter.increment(commands.size());
                }
            );
    }

    private record CommandArguments(String commandName, List<String> arguments) {
        public static CommandArguments fromString(String text) {
            List<String> words = List.of(text.split("\\s+"));
            String commandName = words.getFirst();
            return new CommandArguments(commandName, words.subList(1, words.size()));
        }
    }

    @SuppressWarnings("InnerTypeLast")
    private static SendMessage createMessage(Long chatId, String text) {
        return new SendMessage(chatId, text)
            .parseMode(ParseMode.Markdown)
            .linkPreviewOptions(new LinkPreviewOptions().isDisabled(true));
    }
}
