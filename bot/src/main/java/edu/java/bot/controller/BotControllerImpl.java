package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.LinkPreviewOptions;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final TelegramBot telegramBot;

    @Override
    public void update(LinkUpdateRequest request) {
        String messageText = "Link " + request.url + " has been updated: " + request.description;
        request.tgChatIds.stream().map(createSendMessage(messageText)).forEach(telegramBot::execute);
    }

    private static Function<Long, SendMessage> createSendMessage(String messageText) {
        return chatId -> new SendMessage(chatId, messageText).linkPreviewOptions(
            new LinkPreviewOptions().isDisabled(true)
        );
    }
}
