package edu.baker;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Project11, written one requirement at a time using
 * Test-Driven Development (red, green, refactor).
 */
class Project11Test {

    /**
     * Checks requirements 1, 2 and 3: fibonacci(input) returns the expected
     * Fibonacci number. The rows cover both base cases (0 and 1) and every
     * input from 2 to 10, so the recursive case is checked for small values
     * that can be verified by hand. Rows 20 and 15 test the cache out of
     * order: 20 jumps ahead of 10, so the values 11 to 19 must be computed and
     * cached along the way, and 15 is then read back from that cache. The last
     * row, 92, is the upper boundary: the largest input whose result fits in a long.
     *
     * @param input    the position in the Fibonacci sequence
     * @param expected the correct Fibonacci number for that position
     */
    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "1, 1",
            "2, 1",
            "3, 2",
            "4, 3",
            "5, 5",
            "6, 8",
            "7, 13",
            "8, 21",
            "9, 34",
            "10, 55",
            "20, 6765",
            "15, 610",
            "92, 7540113804746346429"
    })
    void fibonacciTest(int input, long expected) {
        assertEquals(expected, Project11.fibonacci(input));
    }

    /**
     * Checks requirement 4: a negative input throws IllegalArgumentException
     * with the message "fibonacci input must be 0 - " followed by the maximum.
     * -1 is the boundary, the closest invalid value to the valid input 0.
     * -10 is a typical negative value, and Integer.MIN_VALUE is the most
     * extreme negative int, so the whole invalid range below 0 is represented.
     *
     * @param input a negative input that fibonacci() must reject
     */
    @ParameterizedTest
    @ValueSource(ints = {-1, -10, Integer.MIN_VALUE})
    void fibonacciNegativeInputThrowsTest(int input) {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Project11.fibonacci(input));
        assertEquals("fibonacci input must be 0 - " + Project11.MAX_FIBONACCI_INPUT,
                exception.getMessage());
    }

    /**
     * Checks requirement 5: an input large enough to overflow a long throws
     * IllegalArgumentException with the message "fibonacci input must be 0 - "
     * followed by the maximum. 93 is the boundary, the smallest input whose
     * result overflows a long (found by the loop in main). 1,000,000 is far
     * past the limit, and Integer.MAX_VALUE is the most extreme positive int.
     *
     * @param input an input too large for fibonacci() to compute without overflow
     */
    @ParameterizedTest
    @ValueSource(ints = {93, 1000000, Integer.MAX_VALUE})
    void fibonacciTooLargeInputThrowsTest(int input) {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> Project11.fibonacci(input));
        assertEquals("fibonacci input must be 0 - " + Project11.MAX_FIBONACCI_INPUT,
                exception.getMessage());
    }

    /**
     * Checks the first rule of McCarthy 91: an input greater than 100 returns
     * the input minus 10. 101 is the boundary, the smallest input in this
     * partition. 102 is the next value up, 150 and 1000 are typical values,
     * and Integer.MAX_VALUE is the largest possible int.
     *
     * @param input    an input greater than 100
     * @param expected the input minus 10
     */
    @ParameterizedTest
    @CsvSource({
            "101, 91",
            "102, 92",
            "150, 140",
            "1000, 990",
            "2147483647, 2147483637"
    })
    void mcCarthy91AboveHundredTest(int input, int expected) {
        assertEquals(expected, Project11.mcCarthy91(input));
    }

    /**
     * Checks the second rule of McCarthy 91: every input of 100 or less
     * returns 91. 100 is the boundary, the largest input in this partition,
     * and 99 is just below it. 91 is the point where the answer equals the
     * input. 50, 1 and 0 are typical values, and -50 checks that negative
     * inputs also reach 91.
     *
     * @param input an input of 100 or less
     */
    @ParameterizedTest
    @ValueSource(ints = {100, 99, 91, 50, 1, 0, -50})
    void mcCarthy91AtOrBelowHundredTest(int input) {
        assertEquals(91, Project11.mcCarthy91(input));
    }
}