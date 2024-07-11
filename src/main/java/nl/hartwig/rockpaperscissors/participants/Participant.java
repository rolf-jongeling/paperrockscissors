package nl.hartwig.rockpaperscissors.participants;

import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import lombok.NonNull;
import nl.hartwig.rockpaperscissors.game.models.Score;

import java.util.Set;

/**
 * Represents a participant in a game of rock, paper, scissors.
 * <p>
 * Most methods are effectively callback methods, which are called to inform the participants of the game's progress.
 */
public interface Participant {
    /**
     * The name of the participant.
     */
    String name();

    /**
     * The scores of the participant.
     */
    Score getScore();

    /**
     * Indicates that a game is being started.
     *
     * @param participants all participants in this game.
     */
    void startGame(@NonNull Set<Participant> participants);

    /**
     * Indicates that a new round is started.
     *
     * @param roundNumber the number of the round.
     */
    void startRound(int roundNumber);

    /**
     * Indicates that the participant must draw and select a choice.
     *
     * @return the choice made by the participant.
     */
    Choice draw();

    /**
     * Indicates that a round has finished.
     *
     * @param outcome      the outcome of the round.
     * @param participants the participants (and their scores) after the round.
     * @return true, if the participant wants to play another round; false, otherwise.
     */
    boolean finishRound(@NonNull RoundOutcome outcome, @NonNull Set<Participant> participants);

    /**
     * Indicates that the game has finished.
     *
     * @param participants the participants (and their scores) after the round.
     */
    void finishGame(@NonNull Set<Participant> participants);
}
