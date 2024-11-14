package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final LinkUpdateHandler linkUpdateHandler;

    @Override
    public void update(LinkUpdateRequest request) {
        linkUpdateHandler.handle(request);
    }
}
