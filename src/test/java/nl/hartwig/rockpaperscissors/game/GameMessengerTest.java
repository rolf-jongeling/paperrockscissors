package nl.hartwig.rockpaperscissors.game;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameMessengerTest {
    private Participant participantMock1;
    private Participant participantMock2;

    private GameMessenger gameMessenger;

    @BeforeEach
    void setUp() {
        participantMock1 = mock(Participant.class);
        participantMock2 = mock(Participant.class);

        gameMessenger = new GameMessenger(Set.of(participantMock1, participantMock2));
    }

    @Test
    void startGame_WHEN_called_THEN_startGameIsSentToAllParticipants() {
        // Given we have a messenger with two participants.
        Set<Participant> participants = Set.of(participantMock1, participantMock2);
        // When we send startGame.
        gameMessenger.startGame();
        // Then both participants are called.
        verify(participantMock1).startGame(participants);
        verify(participantMock2).startGame(participants);
    }

    @Test
    void startRound_WHEN_called_THEN_startRoundIsSentToAllParticipants() {
        // Given we have a messenger with two participants.
        int roundNumber = 123;
        // When we send startRound.
        gameMessenger.startRound(roundNumber);
        // Then both participants are called.
        verify(participantMock1).startRound(roundNumber);
        verify(participantMock2).startRound(roundNumber);
    }

    @SneakyThrows
    @Test
    void draw_WHEN_called_THEN_drawIsSentToAllParticipants() {
        // Given we have a messenger with two participants and both return a valid choice.
        when(participantMock1.draw()).thenReturn(Choice.PAPER);
        when(participantMock2.draw()).thenReturn(Choice.ROCK);
        // When we send draw.
        Map<Participant, Choice> actualChoices = gameMessenger.draw();
        // Then we have a choice for both participants.
        assertEquals(2, actualChoices.size());
        assertEquals(Choice.PAPER, actualChoices.get(participantMock1));
        assertEquals(Choice.ROCK, actualChoices.get(participantMock2));
    }

    @SneakyThrows
    @Test
    void draw_WHEN_participantIsSlow_THEN_waitForParticipant() {
        // Given we have a messenger with two participants and one is slow to return.
        long waitTime = 1000L;
        when(participantMock1.draw()).thenAnswer(invocation -> {
            Thread.sleep(waitTime);
            return Choice.PAPER;
        });
        when(participantMock2.draw()).thenReturn(Choice.ROCK);
        // When we send draw.
        long startTime = System.currentTimeMillis();
        Map<Participant, Choice> actualChoices = gameMessenger.draw();
        long endTime = System.currentTimeMillis();
        // Then we have a choice for both participants.
        assertEquals(2, actualChoices.size());
        assertEquals(Choice.PAPER, actualChoices.get(participantMock1));
        assertEquals(Choice.ROCK, actualChoices.get(participantMock2));
        // And we have clearly waited for the slower participant.
        assertTrue(endTime - startTime > waitTime);
    }

    @Test
    void finishRound_WHEN_called_THEN_finishRoundIsSentToAllParticipants() {
        // Given we have a messenger with two participants.
        Set<Participant> participants = Set.of(participantMock1, participantMock2);
        // When we send finishRound.
        RoundOutcome roundOutcome = RoundOutcome.withDraws(Collections.emptyMap());
        gameMessenger.finishRound(roundOutcome, participants);
        // Then both participants are called.
        verify(participantMock1).finishRound(roundOutcome, participants);
        verify(participantMock2).finishRound(roundOutcome, participants);
    }

    @Test
    void finishGame_WHEN_called_THEN_finishGameIsSentToAllParticipants() {
        // Given we have a messenger with two participants.
        Set<Participant> participants = Set.of(participantMock1, participantMock2);
        // When we send finishGame.
        gameMessenger.finishGame(participants);
        // Then both participants are called.
        verify(participantMock1).finishGame(participants);
        verify(participantMock2).finishGame(participants);
    }
}