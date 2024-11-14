package edu.java.service.sender;

import edu.java.client.BotClient;
import edu.java.client.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.useQueue", havingValue = "false", matchIfMissing = true)
public class HttpBotSender implements UpdateSender {
    private final BotClient botClient;

    @Override
    public void send(LinkUpdateRequest update) {
        botClient.update(update).subscribe();
    }
}
