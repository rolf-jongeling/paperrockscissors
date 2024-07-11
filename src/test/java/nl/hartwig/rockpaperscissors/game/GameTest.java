package nl.hartwig.rockpaperscissors.game;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static nl.hartwig.rockpaperscissors.TestResources.TEST_PARTICIPANT_0;
import static nl.hartwig.rockpaperscissors.TestResources.TEST_PARTICIPANT_1;
import static org.mockito.Mockito.*;

class GameTest {
    private GameProperties gameProperties;
    private GameMessenger gameMessengerMock;

    private Game game;

    @BeforeEach
    void setUp() {
        gameProperties = new GameProperties(Set.of(TEST_PARTICIPANT_0, TEST_PARTICIPANT_1));
        gameMessengerMock = mock(GameMessenger.class);
        game = new Game(gameProperties, gameMessengerMock);
    }

    @SneakyThrows
    @Test
    void run_WHEN_gameIsStartedWithOneRound_THEN_singleRoundIsPlayed() {
        // Given we have two participants with one having a winning choice.
        Map<Participant, Choice> choices = new HashMap<>();
        choices.put(TEST_PARTICIPANT_0 , Choice.ROCK);
        choices.put(TEST_PARTICIPANT_1, Choice.PAPER);
        when(gameMessengerMock.draw()).thenReturn(choices);
        // And we have a game consisting of one round.
        RoundOutcome roundOutcome = gameProperties.determineOutcome(choices);
        when(gameMessengerMock.finishRound(roundOutcome, gameProperties.getParticipants())).thenReturn(false);

        // When the game is run.
        game.run();

        // Then the game loop is executed correctly.
        verify(gameMessengerMock).startGame();
        verify(gameMessengerMock).startRound(1);
        verify(gameMessengerMock).finishRound(roundOutcome, gameProperties.getParticipants());
        verify(gameMessengerMock).finishGame(gameProperties.getParticipants());
    }

    @SneakyThrows
    @Test
    void run_WHEN_gameIsStartedWithTwoRounds_THEN_twoRoundsArePlayed() {
        gameProperties = new GameProperties(Set.of(TEST_PARTICIPANT_0, TEST_PARTICIPANT_1));
        game = new Game(gameProperties, gameMessengerMock);
        // Given we have two participants with one having a winning choice.
        Map<Participant, Choice> choices = new HashMap<>();
        choices.put(TEST_PARTICIPANT_0, Choice.ROCK);
        choices.put(TEST_PARTICIPANT_1, Choice.PAPER);
        when(gameMessengerMock.draw()).thenReturn(choices);
        // And we have a game consisting of two rounds.
        RoundOutcome roundOutcome = gameProperties.determineOutcome(choices);
        when(gameMessengerMock.finishRound(roundOutcome, gameProperties.getParticipants())).thenReturn(true)
                                                                                     .thenReturn(false);

        // When the game is run.
        game.run();

        // Then the game loop is executed correctly.
        verify(gameMessengerMock).startGame();
        verify(gameMessengerMock).startRound(1);
        verify(gameMessengerMock).startRound(2);
        verify(gameMessengerMock, times(2)).finishRound(roundOutcome, gameProperties.getParticipants());
        verify(gameMessengerMock).finishGame(gameProperties.getParticipants());
    }
}