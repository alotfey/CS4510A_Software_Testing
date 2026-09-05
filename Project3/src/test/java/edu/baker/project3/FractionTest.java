package edu.baker.project3;

import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Fraction class.
 *
 * The tests were written in two passes.
 *
 * The first pass is specification-based. For each method I read the Javadoc,
 * worked out the input partitions it implies (zero, positive, negative,
 * already reduced, needs reducing) and the exceptions it names, then wrote
 * assertions at those boundaries without looking at the method body.
 *
 * The second pass is structural. After running the JaCoCo report I found
 * three branches the first pass had not reached, in abs(), ipow() and
 * equals(). The assertions added for those are marked with a STRUCTURAL
 * comment inside the test for the method they belong to.
 *
 * @author lotfey
 */
public class FractionTest {

    // Shared test values. Fraction never changes itself after construction,
    // so the same objects can safely be reused by every test.
    private static final Fraction FIVE_SEVENTHS = new Fraction(5, 7);
    private static final Fraction NEG_FIVE_SEVENTHS = new Fraction(-5, 7);
    private static final Fraction SEVEN_FIFTHS = new Fraction(7, 5);
    private static final Fraction NEG_SEVEN_FIFTHS = new Fraction(-7, 5);

    public FractionTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of toString method, of class Fraction.
     * toString is also how the other tests read a fraction's stored value,
     * so it is checked against zero, one, a positive and a negative value.
     */
    @Test
    public void testToString() {
        assertEquals("0/1", Fraction.ZERO.toString());
        assertEquals("1/1", Fraction.ONE.toString());
        assertEquals("5/7", FIVE_SEVENTHS.toString());
        assertEquals("-5/7", NEG_FIVE_SEVENTHS.toString());
    }

    /**
     * Test of hashCode method, of class Fraction.
     * The contract is that equal fractions must produce equal hash codes.
     * I assert that relationship rather than a specific number, so the test
     * does not break if the hashing ever changes.
     */
    @Test
    public void testHashCode() {
        assertEquals(FIVE_SEVENTHS.hashCode(), new Fraction(5, 7).hashCode());
        assertEquals(FIVE_SEVENTHS.hashCode(), new Fraction(10, 14).hashCode());
        assertEquals(FIVE_SEVENTHS.hashCode(), new Fraction(-5, -7).hashCode());

        assertNotEquals(FIVE_SEVENTHS.hashCode(), NEG_FIVE_SEVENTHS.hashCode());
    }

    /**
     * Test of equals method, of class Fraction.
     * Partitions: same object, equal value, different numerator,
     * different denominator, null, and an object of another type.
     */
    @Test
    public void testEquals() {
        assertTrue(FIVE_SEVENTHS.equals(FIVE_SEVENTHS));
        assertTrue(FIVE_SEVENTHS.equals(new Fraction(5, 7)));
        assertTrue(FIVE_SEVENTHS.equals(new Fraction(10, 14)));

        assertFalse(FIVE_SEVENTHS.equals(NEG_FIVE_SEVENTHS));
        assertFalse(FIVE_SEVENTHS.equals(null));
        assertFalse(FIVE_SEVENTHS.equals("5/7"));

        // STRUCTURAL. equals ends with "num.equals(...) && denom.equals(...)".
        // Java short-circuits, so when the numerators differ the denominator
        // check never runs. Every false case above had a different numerator,
        // which left the second half of that condition uncovered in JaCoCo.
        // Comparing 5/7 to 5/8 gets past the numerator and fails on the
        // denominator, which is the only way to reach it.
        assertFalse(new Fraction(5, 7).equals(new Fraction(5, 8)));
    }

    /**
     * Test of reduce method, of class Fraction.
     *
     * reduce() is public but it is only ever reached through the two
     * constructors, which call it as their last step. Building a Fraction
     * and printing it is therefore how reduce is observed, and this test
     * doubles as the test for both constructors: the zero-denominator
     * guard and the sign normalization that run just before reduce are
     * checked here too.
     */
    @Test
    public void testReduce() {
        // --- constructor that takes two longs ---

        // A zero denominator is the documented exception case.
        // assertThrows takes the exception we expect and a block of code
        // that should throw it. The "() -> { ... }" part is that block.
        assertThrows(ArithmeticException.class, () -> {
            new Fraction(1, 0);
        });

        // The sign always ends up on the numerator.
        assertEquals("5/7", new Fraction(5, 7).toString());
        assertEquals("-5/7", new Fraction(-5, 7).toString());
        assertEquals("-5/7", new Fraction(5, -7).toString());
        assertEquals("5/7", new Fraction(-5, -7).toString());

        // Numerator and denominator share a factor, so reduce divides it out.
        assertEquals("5/7", new Fraction(35, 49).toString());
        assertEquals("1/2", new Fraction(21, 42).toString());
        assertEquals(FIVE_SEVENTHS, new Fraction(50, 70));

        // Already in lowest terms, so reduce leaves it alone.
        assertEquals("5/7", new Fraction(5, 7).toString());

        // A zero numerator forces the denominator to 1.
        assertEquals("0/1", new Fraction(0, 12345).toString());

        // --- constructor that takes two BigIntegers ---

        assertThrows(ArithmeticException.class, () -> {
            new Fraction(BigInteger.ONE, BigInteger.ZERO);
        });

        assertEquals("5/7",
                new Fraction(BigInteger.valueOf(5), BigInteger.valueOf(7)).toString());
        assertEquals("-5/7",
                new Fraction(BigInteger.valueOf(-5), BigInteger.valueOf(7)).toString());
        assertEquals("-5/7",
                new Fraction(BigInteger.valueOf(5), BigInteger.valueOf(-7)).toString());
        assertEquals("5/7",
                new Fraction(BigInteger.valueOf(-5), BigInteger.valueOf(-7)).toString());

        assertEquals(FIVE_SEVENTHS,
                new Fraction(BigInteger.valueOf(50), BigInteger.valueOf(70)));

        assertEquals("0/1",
                new Fraction(BigInteger.ZERO, BigInteger.valueOf(7)).toString());
    }

    /**
     * Test of add method, of class Fraction.
     */
    @Test
    public void testAdd() {
        assertEquals(new Fraction(74, 35), FIVE_SEVENTHS.add(SEVEN_FIFTHS));
        assertEquals(new Fraction(-74, 35), NEG_FIVE_SEVENTHS.add(NEG_SEVEN_FIFTHS));
        // -5/7 + 7/5 = (-25 + 49)/35 = 24/35, so the sum is positive:
        // the larger term wins.
        assertEquals(new Fraction(24, 35), NEG_FIVE_SEVENTHS.add(SEVEN_FIFTHS));

        // A fraction plus its negation is zero.
        assertEquals(Fraction.ZERO, FIVE_SEVENTHS.add(NEG_FIVE_SEVENTHS));

        // Adding zero changes nothing.
        assertEquals(FIVE_SEVENTHS, FIVE_SEVENTHS.add(Fraction.ZERO));
    }

    /**
     * Test of sub method, of class Fraction.
     */
    @Test
    public void testSub() {
        assertEquals(new Fraction(-24, 35), FIVE_SEVENTHS.sub(SEVEN_FIFTHS));
        assertEquals(new Fraction(74, 35), FIVE_SEVENTHS.sub(NEG_SEVEN_FIFTHS));

        // A fraction minus itself is zero.
        assertEquals(Fraction.ZERO, FIVE_SEVENTHS.sub(FIVE_SEVENTHS));

        // Subtracting zero changes nothing.
        assertEquals(FIVE_SEVENTHS, FIVE_SEVENTHS.sub(Fraction.ZERO));
    }

    /**
     * Test of mult method, of class Fraction.
     */
    @Test
    public void testMult() {
        assertEquals(new Fraction(25, 49), FIVE_SEVENTHS.mult(FIVE_SEVENTHS));
        assertEquals(new Fraction(-1, 1), FIVE_SEVENTHS.mult(NEG_SEVEN_FIFTHS));

        // A fraction times its reciprocal is one.
        assertEquals(Fraction.ONE, FIVE_SEVENTHS.mult(SEVEN_FIFTHS));

        // Anything times zero is zero.
        assertEquals(Fraction.ZERO, FIVE_SEVENTHS.mult(Fraction.ZERO));
    }

    /**
     * Test of div method, of class Fraction.
     * Dividing by zero makes the result denominator zero, which the
     * constructor rejects, so an ArithmeticException comes back out.
     */
    @Test
    public void testDiv() {
        assertEquals(new Fraction(25, 49), FIVE_SEVENTHS.div(SEVEN_FIFTHS));
        assertEquals(new Fraction(-25, 49), FIVE_SEVENTHS.div(NEG_SEVEN_FIFTHS));

        // A fraction divided by itself is one.
        assertEquals(Fraction.ONE, FIVE_SEVENTHS.div(FIVE_SEVENTHS));

        assertThrows(ArithmeticException.class, () -> {
            FIVE_SEVENTHS.div(Fraction.ZERO);
        });
    }

    /**
     * Test of reciprocal method, of class Fraction.
     */
    @Test
    public void testReciprocal() {
        assertEquals(SEVEN_FIFTHS, FIVE_SEVENTHS.reciprocal());
        assertEquals(NEG_SEVEN_FIFTHS, NEG_FIVE_SEVENTHS.reciprocal());

        // Zero has no reciprocal.
        assertThrows(ArithmeticException.class, () -> {
            Fraction.ZERO.reciprocal();
        });
    }

    /**
     * Test of negate method, of class Fraction.
     */
    @Test
    public void testNegate() {
        assertEquals(NEG_FIVE_SEVENTHS, FIVE_SEVENTHS.negate());
        assertEquals(FIVE_SEVENTHS, NEG_FIVE_SEVENTHS.negate());

        // Negating zero is still zero.
        assertEquals(Fraction.ZERO, Fraction.ZERO.negate());
    }

    /**
     * Test of sign method, of class Fraction.
     */
    @Test
    public void testSign() {
        assertEquals(1, FIVE_SEVENTHS.sign());
        assertEquals(1, new Fraction(1, 1000000).sign());

        assertEquals(0, Fraction.ZERO.sign());
        assertEquals(0, new Fraction(0, 7).sign());

        assertEquals(-1, NEG_FIVE_SEVENTHS.sign());
        assertEquals(-1, new Fraction(5, -7).sign());
    }

    /**
     * Test of abs method, of class Fraction.
     */
    @Test
    public void testAbs() {
        assertEquals(FIVE_SEVENTHS, FIVE_SEVENTHS.abs());
        assertEquals(FIVE_SEVENTHS, NEG_FIVE_SEVENTHS.abs());
        assertEquals(Fraction.ZERO, Fraction.ZERO.abs());

        // STRUCTURAL. abs is an if/else. The if branch builds a new Fraction,
        // the else branch returns "this" unchanged. assertEquals cannot tell
        // those apart because both are equal in value, so JaCoCo showed the
        // else branch as uncovered. assertSame compares object identity,
        // which is the only assertion that forces it.
        assertSame(FIVE_SEVENTHS, FIVE_SEVENTHS.abs());
        assertSame(Fraction.ZERO, Fraction.ZERO.abs());
    }

    /**
     * Test of compare method, of class Fraction.
     */
    @Test
    public void testCompare() {
        assertEquals(1, SEVEN_FIFTHS.compare(FIVE_SEVENTHS));
        assertEquals(1, FIVE_SEVENTHS.compare(NEG_FIVE_SEVENTHS));

        assertEquals(0, FIVE_SEVENTHS.compare(new Fraction(10, 14)));

        assertEquals(-1, NEG_FIVE_SEVENTHS.compare(FIVE_SEVENTHS));
        assertEquals(-1, FIVE_SEVENTHS.compare(SEVEN_FIFTHS));
    }

    /**
     * Test of ipow method, of class Fraction.
     * The Javadoc says the power can be positive, negative or zero, and
     * that a zero fraction to a negative power throws, so those are the
     * partitions. Each is checked with both a positive and a negative base.
     */
    @Test
    public void testIpow() {
        // Power of zero.
        assertEquals(Fraction.ONE, FIVE_SEVENTHS.ipow(0));
        assertEquals(Fraction.ONE, NEG_FIVE_SEVENTHS.ipow(0));

        // Positive base, positive power.
        assertEquals(FIVE_SEVENTHS, FIVE_SEVENTHS.ipow(1));
        assertEquals(new Fraction(25, 49), FIVE_SEVENTHS.ipow(2));
        assertEquals(new Fraction(125, 343), FIVE_SEVENTHS.ipow(3));
        assertEquals(new Fraction(30517578125L, 4747561509943L), FIVE_SEVENTHS.ipow(15));

        // Negative base, positive power. Even powers come out positive.
        assertEquals(NEG_FIVE_SEVENTHS, NEG_FIVE_SEVENTHS.ipow(1));
        assertEquals(new Fraction(25, 49), NEG_FIVE_SEVENTHS.ipow(2));
        assertEquals(new Fraction(-125, 343), NEG_FIVE_SEVENTHS.ipow(3));
        assertEquals(new Fraction(-30517578125L, 4747561509943L), NEG_FIVE_SEVENTHS.ipow(15));

        // Negative power inverts the fraction first.
        assertEquals(SEVEN_FIFTHS, FIVE_SEVENTHS.ipow(-1));
        assertEquals(new Fraction(49, 25), FIVE_SEVENTHS.ipow(-2));
        assertEquals(new Fraction(343, 125), FIVE_SEVENTHS.ipow(-3));
        assertEquals(new Fraction(4747561509943L, 30517578125L), FIVE_SEVENTHS.ipow(-15));

        assertEquals(NEG_SEVEN_FIFTHS, NEG_FIVE_SEVENTHS.ipow(-1));
        assertEquals(new Fraction(49, 25), NEG_FIVE_SEVENTHS.ipow(-2));
        assertEquals(new Fraction(-343, 125), NEG_FIVE_SEVENTHS.ipow(-3));

        // Zero to a positive power is zero.
        assertEquals(Fraction.ZERO, Fraction.ZERO.ipow(5));

        // Zero to a negative power has to invert zero first, so it throws.
        assertThrows(ArithmeticException.class, () -> {
            Fraction.ZERO.ipow(-1);
        });

        // STRUCTURAL. ipow uses square-and-multiply: it walks the bits of the
        // exponent and only multiplies into the result when the current bit
        // is 1. The "if (n % 2 == 1)" inside that loop was only ever taken
        // one way. An exponent of 2 is binary 10, so the first pass through
        // the loop takes the false side and the second pass takes the true
        // side. An exponent of 8 is binary 1000, which repeats the false
        // side three times before the final multiply.
        assertEquals(new Fraction(25, 49), FIVE_SEVENTHS.ipow(2));
        assertEquals(new Fraction(390625, 5764801), FIVE_SEVENTHS.ipow(8));
    }
}