package nl.hartwig.rockpaperscissors.game;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static nl.hartwig.rockpaperscissors.TestResources.*;
import static org.junit.jupiter.api.Assertions.*;

class GamePropertiesTest {
    private GameProperties gameProperties;

    @BeforeEach
    void setUp() {
        gameProperties = new GameProperties(Set.of(TEST_PARTICIPANT_0, TEST_PARTICIPANT_1, TEST_PARTICIPANT_2));
    }

    @Test
    void constructor_WHEN_argumentsAreInvalid_THEN_throwException() {
        // Given the number of participants is less than two when creating a game then throw exception.
        assertThrows(IllegalArgumentException.class, () -> new GameProperties(Set.of()));
        assertThrows(IllegalArgumentException.class, () -> new GameProperties(Set.of(TEST_PARTICIPANT_0)));
    }

    @Test
    void increaseRoundNumber_WHEN_called_THEN_roundNumberIsIncreasedByOne() {
        assertEquals(1, gameProperties.getRoundNumber());
        gameProperties.increaseRoundNumber();
        assertEquals(2, gameProperties.getRoundNumber());
    }

    @Test
    void getParticipants_WHEN_creatingNewGameWithParticipants_THEN_participantsAreInTheList() {
        assertTrue(gameProperties.getParticipants().contains(TEST_PARTICIPANT_0));
        assertTrue(gameProperties.getParticipants().contains(TEST_PARTICIPANT_1));
    }

    @Test
    void determineOutcome_WHEN_numberOfChoicesDoesNotMatchParticipants_THEN_throwException() {
        // Given a game with three participants, but only two choices.
        Map<Participant, Choice> testChoices = new HashMap<>();
        testChoices.put(TEST_PARTICIPANT_0, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_1, Choice.ROCK);
        // When determining the outcome based on these choices then we throw an exception.
        assertThrows(IllegalArgumentException.class, () -> gameProperties.determineOutcome(testChoices));
    }

    @Test
    void determineOutcome_WHEN_oneParticipantHasWinningChoiceOutOfThree_THEN_outcomeIsWin() {
        // Given three participants with one of them having an overall winning choice.
        Map<Participant, Choice> testChoices = new HashMap<>();
        testChoices.put(TEST_PARTICIPANT_0, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_1, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_2, Choice.SCISSORS);
        // When determining the outcome based on these choices.
        RoundOutcome roundOutcome = gameProperties.determineOutcome(testChoices);
        // Then we see a winner and two losers.
        assertTrue(roundOutcome.getLosers().contains(TEST_PARTICIPANT_0));
        assertTrue(roundOutcome.getLosers().contains(TEST_PARTICIPANT_1));
        assertTrue(roundOutcome.getWinners().contains(TEST_PARTICIPANT_2));
        assertEquals(testChoices, roundOutcome.getParticipantChoices());
        // And the scores are correct in terms of wins and losses.
        assertEquals(1, TEST_PARTICIPANT_0.getScore().getLosses());
        assertEquals(1, TEST_PARTICIPANT_1.getScore().getLosses());
        assertEquals(1, TEST_PARTICIPANT_2.getScore().getWins());
    }

    @Test
    void determineOutcome_WHEN_oneParticipantHasLosingChoiceOutOfThree_THEN_outcomeIsLoss() {
        // Given three participants with one of them having an overall losing choice.
        Map<Participant, Choice> testChoices = new HashMap<>();
        testChoices.put(TEST_PARTICIPANT_0, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_1, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_2, Choice.ROCK);
        // When determining the outcome based on these choices.
        RoundOutcome roundOutcome = gameProperties.determineOutcome(testChoices);
        // Then we see a loser and two winners.
        assertTrue(roundOutcome.getWinners().contains(TEST_PARTICIPANT_0));
        assertTrue(roundOutcome.getWinners().contains(TEST_PARTICIPANT_1));
        assertTrue(roundOutcome.getLosers().contains(TEST_PARTICIPANT_2));
        assertEquals(testChoices, roundOutcome.getParticipantChoices());
        // And the scores are correct in terms of wins and losses.
        assertEquals(1, TEST_PARTICIPANT_0.getScore().getWins());
        assertEquals(1, TEST_PARTICIPANT_1.getScore().getWins());
        assertEquals(1, TEST_PARTICIPANT_2.getScore().getLosses());

    }

    @Test
    void determineOutcome_WHEN_noParticipantHasWinningChoiceOutOfThree_THEN_outcomeIsDraw() {
        // Given three participants with none of them having an overall winning choice.
        Map<Participant, Choice> testChoices = new HashMap<>();
        testChoices.put(TEST_PARTICIPANT_0, Choice.PAPER);
        testChoices.put(TEST_PARTICIPANT_1, Choice.ROCK);
        testChoices.put(TEST_PARTICIPANT_2, Choice.SCISSORS);
        // When determining the outcome based on these choices.
        RoundOutcome roundOutcome = gameProperties.determineOutcome(testChoices);
        // Then we see no winner and no loser.
        assertTrue(roundOutcome.getWinners().isEmpty());
        assertTrue(roundOutcome.getLosers().isEmpty());
        assertEquals(testChoices, roundOutcome.getParticipantChoices());
        // And the scores are correct in terms of draws.
        assertEquals(1, TEST_PARTICIPANT_0.getScore().getDraws());
        assertEquals(1, TEST_PARTICIPANT_1.getScore().getDraws());
        assertEquals(1, TEST_PARTICIPANT_2.getScore().getDraws());
    }
}