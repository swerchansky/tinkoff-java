package edu.java.bot.command;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

class ListCommandTest {

    private ListCommand listCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        listCommand = new ListCommand();
    }

    @Test
    @DisplayName("execute should return empty list message when no links are tracked")
    void executeShouldReturnEmptyListMessageWhenNoLinksAreTracked() {
        String result = listCommand.execute(Collections.emptyList());

        assertThat(result).isEqualTo("List of tracked links is empty");
    }
}
