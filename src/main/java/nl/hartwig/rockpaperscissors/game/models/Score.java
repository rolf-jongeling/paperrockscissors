package nl.hartwig.rockpaperscissors.game.models;

import lombok.Getter;

@Getter
public class Score {
    private int wins;
    private int losses;
    private int draws;

    public Score() {
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public void addDraw() {
        draws++;
    }

    /**
     * Calculates a total score to help compare participant scores.
     * <p>
     * Calculation uses two points for a win, one point for a draw, and no points for a loss.
     *
     * @return the calculated score.
     */
    public int calculateScore() {
        return wins * 2 + draws;
    }
}
