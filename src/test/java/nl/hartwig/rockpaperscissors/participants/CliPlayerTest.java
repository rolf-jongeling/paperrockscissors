package nl.hartwig.rockpaperscissors.participants;

import nl.hartwig.rockpaperscissors.cli.CommandLine;
import nl.hartwig.rockpaperscissors.game.models.Choice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CliPlayerTest {

    private CliPlayer cliPlayer;

    private CommandLine commandLineMock;

    @BeforeEach
    void setUp() {
        commandLineMock = mock(CommandLine.class);

        cliPlayer = new CliPlayer("TEST", commandLineMock);
    }

    @Test
    void draw_WHEN_validChoiceIsMade_THEN_returnChoice() {
        // Given only the first letter.
        when(commandLineMock.getString(anyString())).thenReturn("p");
        Choice resultingChoice = cliPlayer.draw();
        assertEquals(Choice.PAPER, resultingChoice);
        // Given the full word.
        when(commandLineMock.getString(anyString())).thenReturn("paper");
        resultingChoice = cliPlayer.draw();
        assertEquals(Choice.PAPER, resultingChoice);
        // Given the use of uppercase.
        when(commandLineMock.getString(anyString())).thenReturn("PAP");
        resultingChoice = cliPlayer.draw();
        assertEquals(Choice.PAPER, resultingChoice);
    }

    @Test
    void draw_WHEN_invalidChoiceIsMade_THEN_askAgain() {
        // Given only the first letter.
        when(commandLineMock.getString(anyString()))
                .thenReturn("invalid")
                .thenReturn("p");
        Choice resultingChoice = cliPlayer.draw();
        assertEquals(Choice.PAPER, resultingChoice);
    }
}