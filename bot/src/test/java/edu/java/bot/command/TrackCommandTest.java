package edu.java.bot.command;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TrackCommandTest {

    private TrackCommand trackCommand;

    @BeforeEach
    void setUp() {
        trackCommand = new TrackCommand();
    }

    @Test
    @DisplayName("execute should return instruction message when no arguments are provided")
    void executeShouldReturnInstructionMessageWhenNoArgumentsAreProvided() {
        String result = trackCommand.execute(Collections.emptyList());

        assertThat(result).isEqualTo("""
            To start tracking a link, use the /track command followed by the link
            Example: /track https://www.google.com
            """);
    }

    @Test
    @DisplayName("execute should return not implemented message when one argument is provided")
    void executeShouldReturnNotImplementedMessageWhenOneArgumentIsProvided() {
        String result = trackCommand.execute(List.of("https://www.google.com"));

        assertThat(result).isEqualTo("This command is not implemented yet");
    }
}
