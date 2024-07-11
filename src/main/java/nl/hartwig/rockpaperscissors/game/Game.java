package nl.hartwig.rockpaperscissors.game;


import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import nl.hartwig.rockpaperscissors.participants.Participant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

/**
 * Manages the basic game loop of the rock, paper, scissors game.
 */
@Log4j2
@RequiredArgsConstructor
public class Game {
    private final GameProperties properties;
    private final GameMessenger messenger;

    public void run() throws InterruptedException {
        log.info("Starting game with the following properties: {}", properties);
        runGameLoop();
        log.info("Successfully finished the game.");
    }

    private void runGameLoop() throws InterruptedException {
        messenger.startGame();

        boolean playGame = true;
        while (playGame) {
            playGame = runRound(properties.getRoundNumber());
            if (playGame) {
                properties.increaseRoundNumber();
            }
        }

        messenger.finishGame(properties.getParticipants());
    }

    private boolean runRound(int roundNumber) throws InterruptedException {
        log.info("Starting round {}...", roundNumber);
        messenger.startRound(roundNumber);

        Map<Participant, Choice> choices = messenger.draw();
        // Determine who has won.
        RoundOutcome roundOutcome = properties.determineOutcome(choices);
        log.info("Outcome of round {}: {}", roundNumber, roundOutcome);

        boolean playAgain = messenger.finishRound(roundOutcome, properties.getParticipants());
        log.info("Finished round {}.", roundNumber);
        return playAgain;
    }
}
