package nl.hartwig.rockpaperscissors;

import nl.hartwig.rockpaperscissors.cli.CommandLine;
import nl.hartwig.rockpaperscissors.game.Game;
import nl.hartwig.rockpaperscissors.game.GameMessenger;
import nl.hartwig.rockpaperscissors.game.GameProperties;
import nl.hartwig.rockpaperscissors.participants.CliPlayer;
import nl.hartwig.rockpaperscissors.participants.Participant;
import nl.hartwig.rockpaperscissors.participants.RandomComputer;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class RockPaperScissors {
    public static void main(String[] args) {
        RockPaperScissors game = new RockPaperScissors();
        game.run();
    }

    private void run() {
        log.info("Starting RockPaperScissors...");

        try (Scanner inputScanner = new Scanner(System.in)) {
            CommandLine commandLine = new CommandLine(inputScanner);

            Set<Participant> participants = createParticipants(commandLine);
            var gameProperties = new GameProperties(participants);
            GameMessenger gameMessenger = new GameMessenger(gameProperties.getParticipants());

            Game game = new Game(gameProperties, gameMessenger);
            game.run();

            commandLine.printMessage("Thank you for playing.");
            log.info("Exiting RockPaperScissors.");
        } catch (Exception exc) {
            System.err.println("An error occurred while running the game.");
            log.error("An exception was caught while running the game.", exc);
        }
    }

    private Set<Participant> createParticipants(CommandLine commandLine) {
        commandLine.printMessage("Please select the number of computer opponents you want to play against.");
        int numberOfOpponents = commandLine.getPositiveInteger();

        // Create chosen number of computer opponents.
        Set<Participant> opponents = IntStream.range(0, numberOfOpponents)
                .mapToObj(number -> new RandomComputer("CPU" + number))
                .collect(Collectors.toSet());
        // We always add a new human player.
        CliPlayer cliPlayer = new CliPlayer("Player", commandLine);
        opponents.add(cliPlayer);
        return opponents;
    }
}