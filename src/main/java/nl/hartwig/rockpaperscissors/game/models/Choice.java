package nl.hartwig.rockpaperscissors.game.models;

import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Represents a choice made during a game of rock, paper, scissors.
 */
public enum Choice {
    ROCK("rock"),
    PAPER("paper"),
    SCISSORS("scissors");

    @Getter
    private final String text;

    Choice(String text) {
        this.text = text;
    }

    public static Choice from(@NonNull String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Given value cannot be empty.");
        }

        String lowerCaseValue = value.toLowerCase(Locale.ROOT);
        for (Choice choice : values()) {
            if (choice.text.startsWith(lowerCaseValue)) {
                return choice;
            }
        }
        throw new IllegalArgumentException("No choice matches the given value: " + value);
    }

    public static List<Choice> getChoices() {
        return Arrays.asList(values());
    }
}
