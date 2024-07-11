package nl.hartwig.rockpaperscissors.game.models;

import nl.hartwig.rockpaperscissors.TestResources;
import nl.hartwig.rockpaperscissors.participants.Participant;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoundOutcomeTest {

    @Test
    void getWinners_WHEN_thereAreWinners_THEN_winnersIsNotEmpty() {
        RoundOutcome roundOutcome = RoundOutcome.withWinners(Set.of(TestResources.TEST_PARTICIPANT_0), createTestChoices());
        assertEquals(1, roundOutcome.getWinners().size());
        assertTrue(roundOutcome.getWinners().contains(TestResources.TEST_PARTICIPANT_0));
    }

    @Test
    void getWinners_WHEN_theOutcomeWasDraw_THEN_winnersIsEmpty() {
        RoundOutcome roundOutcome = RoundOutcome.withDraws(createTestChoices());
        assertTrue(roundOutcome.getWinners().isEmpty());
    }

    @Test
    void getLosers_WHEN_thereAreWinners_THEN_losersIsNotEmptyAndDoesNotContainWinners() {
        RoundOutcome roundOutcome = RoundOutcome.withWinners(Set.of(TestResources.TEST_PARTICIPANT_0), createTestChoices());
        assertEquals(2, roundOutcome.getLosers().size());
        assertFalse(roundOutcome.getLosers().contains(TestResources.TEST_PARTICIPANT_0));
    }

    @Test
    void getLosers_WHEN_theOutcomeWasDraw_THEN_losersIsEmpty() {
        RoundOutcome roundOutcome = RoundOutcome.withDraws(createTestChoices());
        assertTrue(roundOutcome.getLosers().isEmpty());
    }

    private Map<Participant, Choice> createTestChoices() {
        return Map.of(TestResources.TEST_PARTICIPANT_0, Choice.PAPER,
                      TestResources.TEST_PARTICIPANT_1, Choice.PAPER,
                      TestResources.TEST_PARTICIPANT_2, Choice.PAPER);
    }
}