package nl.hartwig.rockpaperscissors.game.models;

import lombok.NonNull;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.Value;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents the outcome of a round.
 * <p>
 * Stores the winner or loser and the choices made by all participants.
 */
@Value
public class RoundOutcome {
    Set<Participant> winners;
    Map<Participant, Choice> participantChoices;

    public static RoundOutcome withWinners(@NonNull Set<Participant> winners, @NonNull Map<Participant, Choice> participantChoices) {
        return new RoundOutcome(winners, participantChoices);
    }

    public static RoundOutcome withDraws(@NonNull Map<Participant, Choice> participantChoices) {
        return new RoundOutcome(new HashSet<>(), participantChoices);
    }

    private RoundOutcome(Set<Participant> winners, Map<Participant, Choice> participantChoices) {
        this.winners = winners;
        this.participantChoices = participantChoices;
    }

    public Set<Participant> getLosers() {
        // If there are no winners then there are no losers.
        if (winners.isEmpty()) {
            return new HashSet<>();
        }
        return participantChoices.keySet().stream()
                                 .filter(participant -> !winners.contains(participant))
                                 .collect(Collectors.toSet());
    }
}
