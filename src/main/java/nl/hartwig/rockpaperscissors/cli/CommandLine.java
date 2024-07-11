package nl.hartwig.rockpaperscissors.cli;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Helper class for console input and output.
 * <p>
 * TODO: replace the Scanner solution with one that is better testable.
 */
@RequiredArgsConstructor
public class CommandLine {
    private final Scanner inputScanner;

    public void printLine() {
        System.out.println("--------------------------------------------------");
    }

    public void printMessage(@NonNull String message) {
        System.out.println(message);
    }

    public void printList(@NonNull String message, @NonNull Collection<String> items) {
        System.out.println(message);
        items.forEach(item -> System.out.println(" * " + item));
    }

    public String getString(@NonNull String message) {
        System.out.println(message);
        return inputScanner.nextLine();
    }

    public int getInteger() {
        while (true) {
            try {
                String rawInput = getString("Enter a valid number (followed by an enter).");
                return Integer.parseInt(rawInput);
            } catch (NumberFormatException exc) {
                printMessage("The given value is not a valid number. Please try again.");
            }
        }
    }

    public int getPositiveInteger() {
        int value = getInteger();
        while (value <= 0) {
            printMessage("The number must be greater than 0.");
            value = getInteger();
        }
        return value;
    }

    public boolean getBoolean() {
        while (true) {
            String rawInput = getString("Enter yes or no (followed by an enter).").toLowerCase(Locale.ROOT);
            if (rawInput.equals("y") || rawInput.equals("yes")) {
                return true;
            }
            if (rawInput.equals("n") || rawInput.equals("no")) {
                return false;
            }
            printMessage("The given value is not a valid (yes/no) answer. Please try again.");
        }
    }
}
