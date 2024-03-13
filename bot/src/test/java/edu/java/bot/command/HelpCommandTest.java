package edu.java.bot.command;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class HelpCommandTest {

    @Mock
    private Command mockCommand1;

    @Mock
    private Command mockCommand2;

    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        helpCommand = new HelpCommand(Arrays.asList(mockCommand1, mockCommand2));
    }

    @Test
    @DisplayName("execute should return help message with all commands")
    void executeShouldReturnHelpMessageWithAllCommands() {
        when(mockCommand1.getCommandName()).thenReturn("/command1");
        when(mockCommand1.getDescription()).thenReturn("description1");
        when(mockCommand2.getCommandName()).thenReturn("/command2");
        when(mockCommand2.getDescription()).thenReturn("description2");

        String result = helpCommand.execute(1L, Collections.emptyList()).block();

        assertThat(result).contains("/command1 - description1", "/command2 - description2");
    }
}
