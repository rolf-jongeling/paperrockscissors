package nl.hartwig.rockpaperscissors.game.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChoiceTest {
    @Test
    void from_WHEN_valueIsValid_THEN_choiceIsReturned() {
        // When the input is a valid choice then return the found choice.
        assertEquals(Choice.PAPER, Choice.from("p"));
        assertEquals(Choice.PAPER, Choice.from("pap"));
        assertEquals(Choice.PAPER, Choice.from("paper"));
        assertEquals(Choice.PAPER, Choice.from("PAPER"));
    }

    @Test
    void from_WHEN_valueIsInvalid_THEN_throwException() {
        // When the input is not one of the choices then throw IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> Choice.from(""));
        assertThrows(IllegalArgumentException.class, () -> Choice.from("!invalid!"));
    }
}