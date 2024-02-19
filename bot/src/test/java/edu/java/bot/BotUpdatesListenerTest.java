package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.command.Command;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private List<Command> commands;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private User user;
    @Mock
    private Chat chat;
    @Mock
    private Command command;
    @Mock
    private Stream<Command> stream;

    private BotUpdatesListener botUpdatesListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botUpdatesListener = new BotUpdatesListener(telegramBot, commands);
    }

    @Test
    @DisplayName("process should execute command and send response message when command is found")
    void shouldProcessCommand() {
        setupMocks("/start", "Hello, username!", command);
        botUpdatesListener.process(List.of(update));
        verifyExecution("Hello, username!");
    }

    @Test
    @DisplayName("process should send unknown command message when command is not found")
    void shouldSendUnknownCommand() {
        setupMocks("/unknown", "Unknown command", null);
        botUpdatesListener.process(List.of(update));
        verifyExecution("Unknown command");
    }

    @Test
    @DisplayName("process should not send response message when message is null")
    void shouldNotProcessCommandWhenMessageIsNull() {
        when(update.message()).thenReturn(null);
        botUpdatesListener.process(List.of(update));
        verify(telegramBot, never()).execute(any(SendMessage.class));
    }

    @Test
    @DisplayName("process should respond to multiple updates")
    void shouldProcessMultipleUpdates() {
        setupMocks("/start", "Hello, username!", command);
        botUpdatesListener.process(List.of(update, update, update));
        verify(telegramBot, times(3)).execute(any(SendMessage.class));
    }

    private void setupMocks(String messageText, String commandResponse, Command commandOptional) {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn(messageText);
        when(message.from()).thenReturn(user);
        when(user.username()).thenReturn("username");
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(123L);
        when(commands.stream()).thenReturn(stream);
        when(stream.filter(any())).thenReturn(stream);
        when(stream.findFirst()).thenReturn(commandOptional == null ? Optional.empty() : Optional.of(commandOptional));
        when(command.execute(List.of())).thenReturn(commandResponse);
    }

    private void verifyExecution(String expectedResponse) {
        SendMessage sendMessage = verifyTelegramBotExecution();
        assertThat(sendMessage.getParameters().get("text")).isEqualTo(expectedResponse);
    }

    private SendMessage verifyTelegramBotExecution() {
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}
