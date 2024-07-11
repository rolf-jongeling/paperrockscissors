package nl.hartwig.rockpaperscissors.game.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void constructor_WHEN_objectIsInitialized_THEN_allScoresAreZero() {
        // Given the score object is freshly initialized then all scores are zero.
        assertEquals(0, score.getWins());
        assertEquals(0, score.getLosses());
        assertEquals(0, score.getDraws());
    }

    @Test
    void calculateScore_WHEN_givenValidNumberOfWinsLossesDraws_THEN_calculatedScoreIsCorrect() {
        // Given one result each has been added to the score.
        score.addWin();
        score.addLoss();
        score.addDraw();
        // When calculating the total score then it is correct.
        assertEquals(3, score.calculateScore());

        // When adding another win.
        score.addWin();
        // Then the score is correctly recalculated.
        assertEquals(5, score.calculateScore());

        // When adding another loss.
        score.addLoss();
        // Then the score is correctly recalculated.
        assertEquals(5, score.calculateScore());

        // When adding another draw.
        score.addDraw();
        // Then the score is correctly recalculated.
        assertEquals(6, score.calculateScore());
    }
}