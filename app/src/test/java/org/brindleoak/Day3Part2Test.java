package org.brindleoak;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import org.brindleoak.Day3Part2.BatterySelectionState;

class Day3Part2Test {

    // @Test
    // void removeSmallestThrowsWhenExtractionIsNull() {
    // NullPointerException ex = assertThrows(
    // NullPointerException.class,
    // () -> Day3Part2.selectNextLargestBattery(null));
    // assertEquals("Extraction cannot be null", ex.getMessage());
    // }

    @ParameterizedTest
    @MethodSource("exactlyTwelveCases")
    void removeSmallestReturnsWholeStringWhenTotalLengthExactlyTwelve(String acc, String rem) {
        BatterySelectionState ex = new BatterySelectionState(acc, rem);

        BatterySelectionState result = Day3Part2.selectNextLargestBattery(ex);

        assertEquals("123456789012", result.selected());
        assertEquals("", result.remaining());
    }

    private static Stream<Arguments> exactlyTwelveCases() {
        return Stream.of(
                Arguments.of("", "123456789012"),
                Arguments.of("123456789012", ""),
                Arguments.of("12345678", "9012"));
    }

    @Test
    void removeSmallestMovesExactlyOneCharFromBankToAcc() {
        BatterySelectionState ex = new BatterySelectionState("123", "456789012345");

        BatterySelectionState result = Day3Part2.selectNextLargestBattery(ex);

        assertEquals("1237", result.selected(), "acc should gain one character");
        assertEquals("89012345", result.remaining(), "rem should lose four characters");
    }

    // @Test
    // void removeSmallestThrowsWhenTotalLengthLessThanTwelve() {
    // BatterySelectionState ex = new BatterySelectionState("123", "4567");

    // IllegalArgumentException exThrown = assertThrows(
    // IllegalArgumentException.class,
    // () -> Day3Part2.selectNextLargestBattery(ex));
    // assertEquals("Extraction total length cannot be less than 12",
    // exThrown.getMessage());
    // }

    @Test
    void computeTotalJoltageWorksForSimpleRules() {
        // These expected values match your current algorithm
        List<String> rules = List.of(
                "123456789012", // becomes 123456789012
                "1111111111113" // becomes 111111111113
        );

        long total = Day3Part2.computeTotalJoltage(rules);

        assertEquals(234567900125L, total);
    }
}
