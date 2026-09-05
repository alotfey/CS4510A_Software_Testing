package edu.baker.project3;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Project3 driver class.
 *
 * These tests are structural. The JaCoCo report showed Project3 as
 * completely uncovered, which held the package total below 100% even
 * though Fraction was fully covered.
 *
 * main() only prints a banner, so there is not much behavior to check.
 * Rather than assert nothing at all, the test redirects System.out into a
 * buffer, runs main, and checks what it actually printed. That makes the
 * test verify a real result instead of only touching the line.
 *
 * @author lotfey
 */
public class Project3Test {

    // A buffer to collect whatever main() prints.
    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();

    // A handle on the real System.out so it can be put back afterwards.
    private final PrintStream realSystemOut = System.out;

    public Project3Test() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        // Point System.out at our own buffer, so anything the class under
        // test prints lands in "captured" instead of going to the console.
        System.setOut(new PrintStream(captured));
    }

    @AfterEach
    public void tearDown() {
        // Always put the real System.out back. If this is skipped, every
        // later test and all of Maven's own output would be swallowed.
        System.setOut(realSystemOut);
    }

    /**
     * Test of main method, of class Project3.
     * main takes an array of command line arguments and prints a banner.
     * An empty array is the normal case, since the program does not read
     * any arguments.
     */
    @Test
    public void testMain() {
        Project3.main(new String[0]);

        // trim() removes the line break that println adds, so the test does
        // not depend on whether the platform ends lines with \n or \r\n.
        assertEquals("Project 3!", captured.toString().trim());
    }

    /**
     * Test of the default constructor, of class Project3.
     *
     * Project3 declares no constructor, so Java supplies an implicit one.
     * JaCoCo still counts that constructor as code, and main is static, so
     * calling main never runs it. This test exists to construct the class
     * once. It is a coverage formality rather than a behavior test, which
     * is the kind of code Aniche discusses in section 3.10.6 as not really
     * worth covering.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull(new Project3());
    }
}