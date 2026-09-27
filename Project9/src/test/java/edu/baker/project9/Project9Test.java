package edu.baker.project9;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the refactored, dependency-injected methods in {@link Project9}.
 * Each dependency (input source, today's date, random generator, message service)
 * is supplied by the test, so every expected value is fixed and repeatable.
 *
 * @author Ahmed Lotfey
 */
public class Project9Test {

    // ---------------- countChars ----------------

    /**
     * Supplies the structural test cases for countChars. Each row is
     * (text the StringReader will supply, expected character count).
     * The rows cover the three ways the read loop can run: zero times (empty input),
     * once (one line), and many times (several lines). Two more rows confirm that
     * line terminators (\n and \r\n) are not counted.
     *
     * @return a Stream of Arguments, one per test case (Stream.of is just the
     *         container JUnit expects a @MethodSource method to return)
     */
    static Stream<Arguments> countCharsData() {
        return Stream.of(
                Arguments.of("", 0L),            // empty input: first readLine() is null, loop body never runs
                Arguments.of("Hello", 5L),       // one line: loop body runs once
                Arguments.of("ab\ncd\nef", 6L),  // three lines: loop runs 3 times, the \n are not counted
                Arguments.of("abc\r\n", 3L),     // Windows line ending: \r\n is not counted either
                Arguments.of("\n\n\n", 0L)       // three empty lines: loop runs but adds 0 each time
        );
    }

    /**
     * Verifies that countChars returns the number of characters in the input,
     * not counting line terminators. A StringReader is injected in place of a file,
     * so the business logic is tested without touching the filesystem.
     *
     * @param input    the text the injected StringReader supplies
     * @param expected the expected character count for that text
     */
    @ParameterizedTest
    @MethodSource("countCharsData")
    void countCharsCountsCharactersButNotLineEndings(String input, long expected) {
        StringReader reader = new StringReader(input);
        assertEquals(expected, Project9.countChars(reader));
    }

    /**
     * Verifies that countChars returns 0 when reading fails with an IOException.
     * Reading from a StringReader that is already closed throws
     * IOException("Stream closed"), which drives execution into the catch block.
     * That is the one path the parameterized cases above cannot reach.
     */
    @Test
    void countCharsReturnsZeroWhenReaderThrowsIOException() {
        StringReader closedReader = new StringReader("abc");
        closedReader.close();
        assertEquals(0L, Project9.countChars(closedReader));
    }

    // ---------------- yearsOld ----------------

    /**
     * Supplies the test cases for yearsOld. Each row is
     * (birthday, the fixed "today" to inject, expected age in whole years).
     * The rows sit on the boundary where the age ticks up by one: the day before
     * the birthday, the birthday itself, and the day of birth (age 0). The last three
     * rows cover a February 29 birthday, the trickiest boundary, because in a
     * non-leap year that date does not exist.
     *
     * @return a Stream of Arguments, one per test case
     */
    static Stream<Arguments> yearsOldData() {
        return Stream.of(
                Arguments.of(LocalDate.of(2000, 5, 15), LocalDate.of(2026, 5, 14), 25L), // day before birthday: not 26 yet
                Arguments.of(LocalDate.of(2000, 5, 15), LocalDate.of(2026, 5, 15), 26L), // on the birthday: turns 26
                Arguments.of(LocalDate.of(2000, 5, 15), LocalDate.of(2000, 5, 15), 0L),  // born today: age 0
                Arguments.of(LocalDate.of(2004, 2, 29), LocalDate.of(2005, 2, 28), 0L),  // leap-day baby, Feb 28: not 1 yet
                Arguments.of(LocalDate.of(2004, 2, 29), LocalDate.of(2005, 3, 1), 1L),   // leap-day baby, Mar 1: turns 1
                Arguments.of(LocalDate.of(2004, 2, 29), LocalDate.of(2008, 2, 29), 4L)   // next real Feb 29: exactly 4
        );
    }

    // ---------------- createPokerHand ----------------

    /**
     * The one shared Random object that is injected into every createPokerHand call.
     * The property below reseeds it on each try instead of creating a new Random.
     */
    private static final Random RANDOM = new Random();

    /**
     * Builds a full deck for dealing: a List holding the integers 0 through 51,
     * one per card. A fresh deck is needed on every try because createPokerHand
     * removes the cards it deals.
     *
     * @return a new List of the 52 card values 0 - 51
     */
    private static List<Integer> newDeck() {
        List<Integer> deck = new ArrayList<Integer>();
        for (int card = 0; card < 52; card++) {
            deck.add(card);
        }
        return deck;
    }

    /**
     * Property: dealing 10 five-card hands from one full deck uses 50 different
     * cards and leaves exactly 2 cards in the deck. jqwik runs this 1000 times.
     * On each try jqwik generates a new seed, which is set on the one shared Random
     * before dealing, so every try deals different hands, and a failing try can be
     * replayed exactly from the seed jqwik reports.
     *
     * @param seed the random seed jqwik generates for this try
     */
    @Property(tries = 1000)
    void tenHandsUseFiftyUniqueCardsAndLeaveTwoInDeck(@ForAll long seed) {
        RANDOM.setSeed(seed);
        List<Integer> deck = newDeck();
        HashSet<String> uniqueCards = new HashSet<String>();
        int cardsDealt = 0;

        for (int h = 0; h < 10; h++) {
            String hand = Project9.createPokerHand(deck, RANDOM);
            String[] cards = hand.split(" ");
            for (int c = 0; c < cards.length; c++) {
                uniqueCards.add(cards[c]);
                cardsDealt++;
            }
        }

        assertEquals(50, cardsDealt);          // 10 hands x 5 cards were dealt
        assertEquals(50, uniqueCards.size());  // all 50 cards are different
        assertEquals(2, deck.size());          // 52 - 50 = 2 cards left in the deck
    }

    // ---------------- sendSpam ----------------

    /**
     * The line ending System.out.println uses on this computer ("\n" on Mac and
     * Linux, "\r\n" on Windows), so the expected output matches on any machine.
     */
    private static final String NL = System.lineSeparator();

    /**
     * Supplies the test cases for sendSpam. Each row is
     * (subject, email addresses, exactly what should be printed).
     * The rows cover the three ways the send loop can run: zero times (no
     * addresses), once (one address), and many times (three addresses, which also
     * checks they are sent in list order).
     *
     * @return a Stream of Arguments, one per test case
     */
    static Stream<Arguments> sendSpamData() {
        return Stream.of(
                Arguments.of("Big Sale",
                        new String[] {},
                        ""),
                Arguments.of("Big Sale",
                        new String[] {"ann@example.com"},
                        "ann@example.com: Big Sale" + NL),
                Arguments.of("Meeting at 3",
                        new String[] {"ann@example.com", "bob@example.org", "cara@example.net"},
                        "ann@example.com: Meeting at 3" + NL
                                + "bob@example.org: Meeting at 3" + NL
                                + "cara@example.net: Meeting at 3" + NL)
        );
    }

    /**
     * Verifies that sendSpam sends exactly one message per address, in order,
     * through the injected MessageService. A ConsoleMessageService is injected in
     * place of a real email provider, and ConsoleOutputCapturer records what it
     * prints so the test can compare it with the expected "address: subject" lines.
     *
     * @param subject        the subject line to send
     * @param addresses      the email addresses to send to
     * @param expectedOutput the exact console output expected, one line per address
     */
    @ParameterizedTest
    @MethodSource("sendSpamData")
    void sendSpamPrintsOneAddressAndSubjectLinePerAddress(String subject, String[] addresses, String expectedOutput) {
        Iterator<String> addrList = Arrays.asList(addresses).iterator();
        ConsoleOutputCapturer capturer = new ConsoleOutputCapturer();

        capturer.start();
        Project9.sendSpam(subject, "Message body", addrList, new ConsoleMessageService());
        String output = capturer.stop();

        assertEquals(expectedOutput, output);
    }

    /**
     * Verifies that yearsOld returns the correct age in whole years when a fixed
     * date is injected as "today". Because the test supplies the date instead of
     * the method calling LocalDate.now(), the expected values never go out of date.
     *
     * @param birthday the birthday to pass in
     * @param today    the fixed date injected in place of LocalDate.now()
     * @param expected the expected age in whole years
     */
    @ParameterizedTest
    @MethodSource("yearsOldData")
    void yearsOldReturnsWholeYearsBetweenBirthdayAndInjectedToday(LocalDate birthday, LocalDate today, long expected) {
        assertEquals(expected, Project9.yearsOld(birthday, today));
    }
}