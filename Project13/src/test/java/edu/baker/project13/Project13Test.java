package edu.baker.project13;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Project13.main(), the console launcher for the RPN calculator.
 * main() reads from a file or from the keyboard (System.in) and prints to
 * the console (System.out), so these tests swap both for in-memory streams.
 * That way each test can type input and read the output, then restore the
 * real console afterwards.
 */
class Project13Test {

    /** The real console output, saved so it can be put back after each test. */
    private final PrintStream originalOut = System.out;

    /** The real keyboard input, saved so it can be put back after each test. */
    private final InputStream originalIn = System.in;

    /** Collects everything main() prints during one test. */
    private ByteArrayOutputStream captured;

    /**
     * Runs before every test. Points System.out at an in-memory buffer so the
     * test can read what main() printed.
     */
    @BeforeEach
    void captureConsoleOutput() {
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true));
    }

    /**
     * Runs after every test, even one that fails. Puts the real System.out
     * and System.in back, so the other test classes still print to the
     * console normally.
     */
    @AfterEach
    void restoreConsole() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    /**
     * Checks that main() runs a script file when its path is passed as the
     * first argument, the same way the Run Configuration did in step 1.
     * This covers the args.length >= 1 branch.
     */
    @Test
    void testMainRunsScriptFileGivenAsArgument() {
        Project13.main(new String[] {"test_cases/circle_diameter.rpn"});

        String s = captured.toString();
        assertTrue(s.contains("RPN Calculator!"), "Missing the banner");
        assertTrue(s.contains("r=20"), "The script was not processed");
        assertFalse(s.contains("An exception occured"), "main() reported an error");
    }

    /**
     * Checks that main() reads commands from the keyboard when no argument is
     * given. The test "types" 10, 3, sub, exit by replacing System.in with a
     * stream holding those lines, and expects 10 - 3 = 7.0 in the output.
     * This covers the else branch (no arguments).
     */
    @Test
    void testMainReadsFromConsoleWhenNoArgumentGiven() {
        String typed = "10\n3\nsub\nexit\n";
        System.setIn(new ByteArrayInputStream(typed.getBytes(StandardCharsets.UTF_8)));

        Project13.main(new String[0]);

        String s = captured.toString();
        assertTrue(s.contains("Enter one value or function name per line"),
                "Missing the console instructions");
        assertTrue(s.contains("7.0"), "10 3 sub should give 7.0");
    }

    /**
     * Checks that main() reports a missing script file instead of crashing.
     * Opening a file that does not exist throws NoSuchFileException, which
     * main() catches and prints. This covers the catch block.
     * Note: "occured" is misspelled in Project13.java itself, so the test
     * must match that exact spelling.
     */
    @Test
    void testMainReportsMissingScriptFile() {
        Project13.main(new String[] {"test_cases/does_not_exist.rpn"});

        String s = captured.toString();
        assertTrue(s.contains("An exception occured: NoSuchFileException"),
                "main() should report the missing file");
    }
}