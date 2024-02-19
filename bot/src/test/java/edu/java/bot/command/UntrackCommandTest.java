package edu.java.bot.command;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UntrackCommandTest {
    private UntrackCommand untrackCommand;

    @BeforeEach
    void setUp() {
        untrackCommand = new UntrackCommand();
    }

    @Test
    @DisplayName("execute should return instruction message when no arguments are provided")
    void executeShouldReturnInstructionMessageWhenNoArgumentsAreProvided() {
        String result = untrackCommand.execute(Collections.emptyList());

        assertThat(result).isEqualTo("""
            To stop tracking a link, use the /untrack command followed by the link
            Example: /untrack https://www.google.com
            """);
    }

    @Test
    @DisplayName("execute should return not implemented message when one argument is provided")
    void executeShouldReturnNotImplementedMessageWhenOneArgumentIsProvided() {
        String result = untrackCommand.execute(List.of("https://www.google.com"));

        assertThat(result).isEqualTo("This command is not implemented yet");
    }
}
