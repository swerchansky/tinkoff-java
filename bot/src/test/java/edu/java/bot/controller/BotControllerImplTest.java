package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import java.net.URI;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BotControllerImplTest {
    @Mock
    private LinkUpdateHandler linkUpdateHandler;

    private BotControllerImpl botController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botController = new BotControllerImpl(linkUpdateHandler);
    }

    @Test
    @DisplayName("Should send update message to all chat ids")
    void update() {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.url = URI.create("http://example.com");
        request.description = "Example description";
        request.tgChatIds = Arrays.asList(123L, 456L);

        botController.update(request);

        verify(linkUpdateHandler, times(1)).handle(any(LinkUpdateRequest.class));
    }
}
