package nl.hartwig.rockpaperscissors.participants;

import nl.hartwig.rockpaperscissors.cli.CommandLine;
import nl.hartwig.rockpaperscissors.game.models.Choice;
import nl.hartwig.rockpaperscissors.game.models.RoundOutcome;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import nl.hartwig.rockpaperscissors.game.models.Score;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Participant of a rock, paper, scissors game that communicates via the CLI.
 * <p>
 * This class can be used to allow human players to participate.
 */
@Log4j2
public class CliPlayer implements Participant {
    private final String name;
    private final CommandLine commandLine;
    private final Score score;

    public CliPlayer(@NonNull String name, @NonNull CommandLine commandLine) {
        this.name = name;
        this.commandLine = commandLine;
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
    public void startGame(Set<Participant> participants) {
        List<String> participantNames = participants.stream().map(Participant::name).toList();
        commandLine.printList("Starting game with the following candidates:", participantNames);
    }

    @Override
    public void startRound(int roundNumber) {
        commandLine.printLine();
        commandLine.printMessage("Starting round number " + roundNumber);
    }

    @Override
    public Choice draw() {
        List<String> choices = Choice.getChoices().stream().map(Choice::getText).toList();
        commandLine.printList("Select one of the following choices:", choices);
        return getChoiceFromCli();
    }

    private Choice getChoiceFromCli() {
        while (true) {
            try {
                String input = commandLine.getString("Please enter your choice (followed by an enter).");
                Choice choice = Choice.from(input);
                commandLine.printMessage("You have selected: " + choice);
                return choice;
            } catch (IllegalArgumentException exc) {
                log.warn("User made an invalid choice.");
                commandLine.printMessage("Your input was invalid. Please try again.");
            }
        }
    }

    @Override
    public boolean finishRound(@NonNull RoundOutcome roundOutcome, @NonNull Set<Participant> participants) {
        String outcomeMessage = "The round has finished and ";
        if (!roundOutcome.getWinners().isEmpty()) {
            outcomeMessage += "the winners are: ";
            commandLine.printList(outcomeMessage, roundOutcome.getWinners().stream().map(Participant::name).collect(Collectors.toSet()));
        } else {
            outcomeMessage += "it was a draw.";
            commandLine.printMessage(outcomeMessage);
        }

        List<String> choices = new ArrayList<>(roundOutcome.getParticipantChoices().size());
        roundOutcome.getParticipantChoices().forEach((participant, choice) -> choices.add(participant.name() + ": " + choice.getText()));
        commandLine.printList("Participant choices: ", choices);

        printScores("The current scores are:", participants);

        commandLine.printMessage("Would you like to play again?");
        return commandLine.getBoolean();
    }

    @Override
    public void finishGame(@NonNull Set<Participant> participants) {
        commandLine.printLine();
        commandLine.printMessage("The game has finished.");
        printScores("The final scores are:", participants);
        printWinners(participants);
    }

    private void printScores(String message, Set<Participant> participants) {
        List<String> scoreTexts = participants.stream()
                                              .map(this::scoreText)
                                              .collect(Collectors.toList());
        commandLine.printList(message, scoreTexts);
    }

    private String scoreText(Participant participant) {
        return participant.name() +
                ": " +
                participant.getScore().getWins() +
                " wins, " +
                participant.getScore().getLosses() +
                " losses, " +
                participant.getScore().getDraws() +
                " draws.";
    }

    private void printWinners(Set<Participant> participants) {
        Set<String> winningParticipants = determineWinners(participants);
        if (winningParticipants.size() == participants.size()) {
            commandLine.printMessage("The final result is a draw.");
            return;
        }
        commandLine.printList("The winners are:", winningParticipants);
        if (winningParticipants.contains(this.name)) {
            commandLine.printMessage("Congratulations!");
        } else {
            commandLine.printMessage("Better luck next time.");
        }
    }

    private static Set<String> determineWinners(Set<Participant> participants) {
        var finalScores = participants.stream()
                                      .collect(Collectors.toMap(Function.identity(), p -> p.getScore().calculateScore()));
        int highScore = finalScores.values()
                                   .stream()
                                   .mapToInt(score -> score)
                                   .max()
                                   .orElseThrow();
        return finalScores.entrySet()
                          .stream()
                          .filter(entry -> entry.getValue() == highScore)
                          .map(entry -> entry.getKey().name())
                          .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof CliPlayer cliPlayer) {
            return name.equals(cliPlayer.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
