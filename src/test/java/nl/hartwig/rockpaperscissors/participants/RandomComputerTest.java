package nl.hartwig.rockpaperscissors.participants;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomComputerTest {

    private RandomComputer randomComputer;

    @BeforeEach
    void setUp() {
        randomComputer = new RandomComputer("TEST");
    }

    @Test
    void draw_WHEN_called_THEN_returnsValidChoice() {
        // When draw is called then a valid choice is returned.
        for (int i = 0; i < 10; i++) {
            Choice choice = randomComputer.draw();
            assertNotNull(choice);
            assertTrue(Choice.getChoices().contains(choice));
        }
    }

    @Test
    void finishRound_WHEN_called_THEN_alwaysReturnTrue() {
        RoundOutcome roundOutcome = RoundOutcome.withDraws(Map.of(randomComputer, Choice.PAPER));
        assertTrue(randomComputer.finishRound(roundOutcome, Set.of(randomComputer)));
    }
}