package org.brindleoak;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import org.brindleoak.Day3Part2.Extraction;

class Day3Part2Test {

    @Test
    void removeSmallestThrowsWhenExtractionIsNull() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Day3Part2.removeSmallest(null));
        assertEquals("Extraction cannot be null", ex.getMessage());
    }

    @Test
    void removeSmallestReturnsWholeStringWhenTotalLengthExactlyTwelve1() {
        Extraction ex = new Extraction("", "123456789012");

        Extraction result = Day3Part2.removeSmallest(ex);

        assertEquals("123456789012", result.acc());
        assertEquals("", result.rem());
    }

    @Test
    void removeSmallestReturnsWholeStringWhenTotalLengthExactlyTwelve2() {
        Extraction ex = new Extraction("123456789012", "");

        Extraction result = Day3Part2.removeSmallest(ex);

        assertEquals("123456789012", result.acc());
        assertEquals("", result.rem());
    }

    @Test
    void removeSmallestReturnsWholeStringWhenTotalLengthExactlyTwelve3() {
        Extraction ex = new Extraction("12345678", "9012");

        Extraction result = Day3Part2.removeSmallest(ex);

        assertEquals("123456789012", result.acc());
        assertEquals("", result.rem());
    }

    @Test
    void removeSmallestMovesExactlyOneCharFromBankToAcc() {
        Extraction ex = new Extraction("123", "456789012345");

        Extraction result = Day3Part2.removeSmallest(ex);

        // length invariants
        assertEquals("1237", result.acc(), "acc should gain one character");
        assertEquals("89012345", result.rem(), "rem should lose four characters");
    }

    @Test
    void removeSmallestThrowsWhenTotalLengthLessThanTwelve() {
        Extraction ex = new Extraction("123", "4567"); // length 7 < 12

        IllegalArgumentException exThrown = assertThrows(
                IllegalArgumentException.class,
                () -> Day3Part2.removeSmallest(ex));
        assertEquals("Extraction total length cannot be less than 12", exThrown.getMessage());
    }

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
