package nl.hartwig.rockpaperscissors.game;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facilitates the communication between the game and the participants.
 * <p>
 * It sends for example events such as the start of a new round to all known participants.
 */
@Log4j2
@RequiredArgsConstructor
public class GameMessenger {
    private final Set<Participant> participants;

    public void startGame() {
        sendToAllParticipants(participant -> participant.startGame(participants));
    }

    public void startRound(int roundNumber) {
        sendToAllParticipants(participant -> participant.startRound(roundNumber));
    }

    public Map<Participant, Choice> draw() throws InterruptedException {
        Map<Participant, Choice> choices = getFromAllParticipants(Participant::draw);
        log.info("Collected the following choices: {}", choices);
        return choices;
    }

    public boolean finishRound(@NonNull RoundOutcome roundOutcome, @NonNull Set<Participant> participants) {
        Map<Participant, Boolean> responses = getFromAllParticipants(participant -> participant.finishRound(roundOutcome, participants));
        log.info("Collected the following responses: {}", responses);
        return responses.values().stream().allMatch(response -> response);
    }

    public void finishGame(@NonNull Set<Participant> participants) {
        sendToAllParticipants(participant -> participant.finishGame(participants));
    }

    private void sendToAllParticipants(Consumer<Participant> message) {
        log.debug("Sending message to all participants...");
        participants.forEach(message);
        log.debug("Successfully sent message to all participants.");
    }

    private <T> Map<Participant, T> getFromAllParticipants(Function<Participant, T> message) {
        log.debug("Sending message to all participants and getting response...");
        Map<Participant, T> responses = participants.stream()
                                                    .collect(Collectors.toMap(Function.identity(), message));
        log.debug("Successfully sent message to all participants and collected all responses.");
        return responses;
    }
}
