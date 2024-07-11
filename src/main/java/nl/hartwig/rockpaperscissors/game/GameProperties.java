package nl.hartwig.rockpaperscissors.game;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.*;

/**
 * Represents the basic game and its properties, such as rules and participants.
 */
@Data
@Log4j2
public class GameProperties {
    private enum Result {
        WINNER,
        LOSER,
        DRAW
    }

    private int roundNumber;
    private final Set<Participant> participants;

    public GameProperties(@NonNull Set<Participant> participants) {
        if (participants.size() < 2) {
            throw new IllegalArgumentException("The game needs at least two participants.");
        }
        this.participants = participants;
        this.roundNumber = 1;
    }

    public void increaseRoundNumber() {
        roundNumber++;
    }

    public RoundOutcome determineOutcome(@NonNull Map<Participant, Choice> choices) {
        if (choices.size() != participants.size()) {
            log.error("The number of choices does not match the number of participants.");
            throw new IllegalArgumentException("The number of choices does not match the number of participants.");
        }

        RoundOutcome roundOutcome = getOutcomeFromChoices(choices);
        updateScores(roundOutcome);
        return roundOutcome;
    }

    /**
     * Get round outcome based on the participant's choices.
     * <p>
     * There are three possible outcomes:
     * * We have a single winner, i.e. a participant has beaten everyone else.
     * * We have a single loser, i.e. a participant was beaten by everyone else.
     * * We have a draw, i.e. there is no single winner or loser.
     * If we find a winner then we don't automatically have a loser.
     *
     * @param choices
     * @return
     */
    private RoundOutcome getOutcomeFromChoices(Map<Participant, Choice> choices) {
        // If all choices are present in the participant's choices then it must be a draw.
        Set<Choice> allChoices = new HashSet<>(choices.values());
        if (allChoices.size() == Choice.getChoices().size()) {
            return RoundOutcome.withDraws(choices);
        }

        Set<Participant> winners = new HashSet<>();
        List<Participant> participants = new ArrayList<>(choices.keySet());
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = participants.get(i);
            for (int j = i + 1; j < participants.size(); j++) {
                Participant otherParticipant = participants.get(j);
                Result result = determineResult(participant, otherParticipant, choices);
                switch (result) {
                    case WINNER -> {
                        winners.add(participant);
                    }
                    case LOSER -> {
                        winners.add(otherParticipant);
                    }
                    default -> log.debug("Game was a draw.");
                }
            }
        }

        return RoundOutcome.withWinners(winners, choices);
    }

    private Result determineResult(Participant left, Participant right, Map<Participant, Choice> choices) {
        Choice leftChoice = choices.get(left);
        Choice rightChoice = choices.get(right);
        if (beats(leftChoice, rightChoice)) {
            return Result.WINNER;
        } else if (beats(rightChoice, leftChoice)) {
            return Result.LOSER;
        }
        return Result.DRAW;
    }

    private boolean beats(Choice left, Choice right) {
        return (left == Choice.PAPER && right == Choice.ROCK)
                || (left == Choice.SCISSORS && right == Choice.PAPER)
                || (left == Choice.ROCK && right == Choice.SCISSORS);
    }

    private void updateScores(RoundOutcome roundOutcome) {
        if (!roundOutcome.getWinners().isEmpty()) {
            roundOutcome.getWinners().forEach(participant -> participant.getScore().addWin());
            roundOutcome.getLosers().forEach(participant -> participant.getScore().addLoss());
        } else {
            getParticipants().forEach(participant -> participant.getScore().addDraw());
        }
    }
}
