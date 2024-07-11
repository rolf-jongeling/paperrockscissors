package nl.hartwig.rockpaperscissors.participants;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import nl.hartwig.rockpaperscissors.game.models.Score;

import java.util.List;
import java.util.Set;

/**
 * Computer participant in a game of rock, paper, scissors.
 * <p>
 * This computer participant makes its choices randomly.
 */
@Log4j2
public class RandomComputer implements Participant {
    private final String name;
    private final Score score;

    public RandomComputer(@NonNull String name) {
        this.name = name;
        this.score = new Score();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public void startGame(@NonNull Set<Participant> participants) {
        log.debug("Game is starting with the following participants: {}", participants);
    }

    @Override
    public void startRound(int roundNumber) {
        log.debug("Starting round {}.", roundNumber);
    }

    @Override
    public Choice draw() {
        log.debug("Drawing by randomly selecting a choice...");
        List<Choice> validChoices = Choice.getChoices();
        int randomChoiceIndex = (int) (Math.random() * validChoices.size());
        Choice randomChoice = validChoices.get(randomChoiceIndex);
        log.info("Selected {} based on the random choice of {}.", randomChoice, randomChoiceIndex);
        return randomChoice;
    }

    @Override
    public boolean finishRound(@NonNull RoundOutcome outcome, @NonNull Set<Participant> participants) {
        log.debug("Round finished. Outcome: {}. Participants: {}", outcome, participants);
        // The computer always wants to play another round.
        return true;
    }

    @Override
    public void finishGame(@NonNull Set<Participant> participants) {
        log.debug("Game finished. Final outcome: {}", participants);
    }
}
