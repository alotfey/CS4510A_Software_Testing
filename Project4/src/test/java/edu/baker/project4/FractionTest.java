package edu.baker.project4;

//https://medium.com/capital-one-tech/improve-java-code-with-unit-tests-and-jacoco-b342643736ed
import java.math.BigInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rich
 */
public class FractionTest {

    private final static Fraction fiveSevenths1 = new Fraction(5, 7);
    private final static Fraction fiveSevenths2 = new Fraction(-5, 7);
    private final static Fraction fiveSevenths3 = new Fraction(5, -7);
    private final static Fraction fiveSevenths4 = new Fraction(-5, -7);
    private final static Fraction sevenFifths1 = new Fraction(7, 5);
    private final static Fraction sevenFifths2 = new Fraction(-7, 5);
    private final static Fraction fourNinths = new Fraction(4, 9);

    // Newton's Method in root() stops when the relative change is under 1e-8,
    // so results are close to the true root but not exactly equal to it.
    private final static double TOLERANCE = 1e-6;

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
     * Test of constructor method that takes longs, of class Fraction.
     */
    @Test
    public void testConstructor1() {
        System.out.println("constructor 1");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = new Fraction(1, 0);
        });
    }

    /**
     * Test of constructor method that takes BigIntegers, of class Fraction.
     */
    @Test
    public void testConstructor2() {
        System.out.println("constructor 2");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = new Fraction(BigInteger.ONE, BigInteger.ZERO);
        });
        assertEquals(fiveSevenths1, new Fraction(BigInteger.valueOf(5), BigInteger.valueOf(7)));
        assertEquals(fiveSevenths2, new Fraction(BigInteger.valueOf(5), BigInteger.valueOf(-7)));
    }

    /**
     * Test of isValid method, of class Fraction. Every Fraction built through a
     * constructor must be valid, including zero, which reduce() stores as 0/1.
     */
    @Test
    public void testIsValid() {
        System.out.println("isValid");
        assertTrue(Fraction.ZERO.isValid());
        assertTrue(Fraction.ONE.isValid());
        assertTrue(fiveSevenths1.isValid());
        assertTrue(fiveSevenths2.isValid());
        assertTrue(new Fraction(0, 7).isValid());   // reduces to 0/1
        assertTrue(new Fraction(BigInteger.ZERO, BigInteger.TEN).isValid());
    }

    /**
     * Test of toString method, of class Fraction.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        assertEquals("0/1", Fraction.ZERO.toString());
        assertEquals("5/7", fiveSevenths1.toString());
        assertEquals("-5/7", fiveSevenths2.toString());
        assertEquals("-5/7", fiveSevenths3.toString());
        assertEquals("5/7", fiveSevenths4.toString());
    }

    /**
     * Test of doubleValue method, of class Fraction.
     */
    @Test
    public void testDoubleValue() {
        System.out.println("doubleValue");
        // These values are exact in binary, so no tolerance is needed.
        assertEquals(0.0, Fraction.ZERO.doubleValue());
        assertEquals(1.0, Fraction.ONE.doubleValue());
        assertEquals(0.5, new Fraction(1, 2).doubleValue());
        assertEquals(-2.5, new Fraction(-5, 2).doubleValue());
        // Repeating decimals: compare within a tiny delta.
        assertEquals(5.0 / 7.0, fiveSevenths1.doubleValue(), 1e-15);
        assertEquals(-5.0 / 7.0, fiveSevenths2.doubleValue(), 1e-15);
        assertEquals(1.0 / 3.0, new Fraction(1, 3).doubleValue(), 1e-15);
    }

    /**
     * Test of hashCode method, of class Fraction.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        assertEquals(1123, fiveSevenths1.hashCode());
        assertEquals(813, fiveSevenths2.hashCode());
        assertEquals(813, fiveSevenths3.hashCode());
        assertEquals(1123, fiveSevenths4.hashCode());
    }

    /**
     * Test of equals method, of class Fraction.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        assertTrue(fiveSevenths1.equals(fiveSevenths1));
        assertTrue(fiveSevenths1.equals(new Fraction(5, 7)));
        assertFalse(fiveSevenths1.equals(fiveSevenths2));
        assertFalse(fiveSevenths1.equals(new Fraction(5, 8)));
        assertFalse(fiveSevenths1.equals(null));    // Never equal to null
        assertFalse(fiveSevenths1.equals("5/7"));   // Not a Fraction
    }

    /**
     * Test of reduce method, of class Fraction. Reduce is called by the
     * constructors so we essentially test the constructors here.
     */
    @Test
    public void testReduce() {
        System.out.println("reduce");
        assertEquals(Fraction.ZERO, new Fraction(0, 7));
        assertEquals(fiveSevenths1, new Fraction(5, 7));
        assertEquals(fiveSevenths1, new Fraction(5 * 10, 7 * 10));
        assertEquals(fiveSevenths2, new Fraction(-5 * 7, 7 * 7));
    }

    /**
     * Test of add method, of class Fraction.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        assertEquals(new Fraction(74, 35), fiveSevenths1.add(sevenFifths1));
        assertEquals(Fraction.ZERO, fiveSevenths1.add(fiveSevenths2));
        assertEquals(new Fraction(-24, 35), fiveSevenths1.add(sevenFifths2));
    }

    /**
     * Precondition: the argument to add() can not be null.
     */
    @Test
    public void testAddNullArgument() {
        System.out.println("add null argument");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.add(null);
        });
        assertEquals("Argument can not be null!", ex.getMessage());
    }

    /**
     * Test of sub method, of class Fraction.
     */
    @Test
    public void testSub() {
        System.out.println("sub");
        assertEquals(new Fraction(-24, 35), fiveSevenths1.sub(sevenFifths1));
        assertEquals(Fraction.ZERO, fiveSevenths1.sub(fiveSevenths1));
        assertEquals(new Fraction(74, 35), fiveSevenths1.sub(sevenFifths2));
    }

    /**
     * Precondition: the argument to Sub() can not be null.
     */
    @Test
    public void testSubNullArgument() {
        System.out.println("sub null argument");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.sub(null);
        });
        assertEquals("Argument can not be null!", ex.getMessage());
    }

    /**
     * Test of mult method, of class Fraction.
     */
    @Test
    public void testMult() {
        System.out.println("mult");
        assertEquals(Fraction.ONE, fiveSevenths1.mult(sevenFifths1));
        assertEquals(new Fraction(25, 49), fiveSevenths1.mult(fiveSevenths1));
        assertEquals(new Fraction(-25, 49), fiveSevenths1.mult(fiveSevenths2));
    }

    /**
     * Precondition: the argument to Mult() can not be null.
     */
    @Test
    public void testMultNullArgument() {
        System.out.println("mult null argument");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.mult(null);
        });
        assertEquals("Argument can not be null!", ex.getMessage());
    }

    /**
     * Test of div method, of class Fraction.
     */
    @Test
    public void testDiv() {
        System.out.println("div");
        assertEquals(new Fraction(25, 49), fiveSevenths1.div(sevenFifths1));
        assertEquals(Fraction.ONE, fiveSevenths1.div(fiveSevenths1));
        assertEquals(new Fraction(-25, 49), fiveSevenths1.div(sevenFifths2));
    }

    /**
     * Precondition: the argument to Div() can not be null.
     */
    @Test
    public void testDivNullArgument() {
        System.out.println("div null argument");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.div(null);
        });
        assertEquals("Argument can not be null!", ex.getMessage());
    }

    /**
     * Test of reciprocal method, of class Fraction.
     */
    @Test
    public void testReciprocal() {
        System.out.println("reciprocal");
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = Fraction.ZERO.reciprocal();
        });
        assertEquals(sevenFifths1, fiveSevenths1.reciprocal());
        assertEquals(sevenFifths2, fiveSevenths2.reciprocal());
    }

    /**
     * Test of negate method, of class Fraction.
     */
    @Test
    public void testNegate() {
        System.out.println("negate");
        assertEquals(fiveSevenths2, fiveSevenths1.negate());
        assertEquals(fiveSevenths1, fiveSevenths2.negate());
    }

    /**
     * Test of sign method, of class Fraction.
     */
    @Test
    public void testSign() {
        System.out.println("sign");
        assertEquals(1, fiveSevenths1.sign());
        assertEquals(0, Fraction.ZERO.sign());
        assertEquals(-1, fiveSevenths2.sign());
    }

    /**
     * Test of abs method, of class Fraction.
     */
    @Test
    public void testAbs() {
        System.out.println("abs");
        assertEquals(fiveSevenths1, fiveSevenths1.abs());
        assertEquals(fiveSevenths1, fiveSevenths2.abs());
    }

    /**
     * Test of compare method, of class Fraction.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        assertEquals(1, fiveSevenths1.compare(fiveSevenths2));
        assertEquals(0, fiveSevenths1.compare(fiveSevenths4));
        assertEquals(-1, fiveSevenths2.compare(fiveSevenths1));
    }

    /**
     * Precondition: the argument to Compare() can not be null.
     */
    @Test
    public void testCompareNullArgument() {
        System.out.println("compare null argument");
        Error ex = assertThrows(AssertionError.class, () -> {
            int c = fiveSevenths1.compare(null);
        });
        assertEquals("Argument can not be null!", ex.getMessage());
    }

    /**
     * Test of ipow method, of class Fraction.
     */
    @Test
    public void testIpow() {
        System.out.println("ipow");
        assertEquals(Fraction.ZERO, Fraction.ZERO.ipow(5));
        // positive to positive power
        assertEquals(Fraction.ONE, fiveSevenths1.ipow(0));
        assertEquals(fiveSevenths1, fiveSevenths1.ipow(1));
        assertEquals(new Fraction(25, 49), fiveSevenths1.ipow(2));
        assertEquals(new Fraction(125, 343), fiveSevenths1.ipow(3));
        assertEquals(new Fraction(30517578125L, 4747561509943L), fiveSevenths1.ipow(15));
        // negative to positive power
        assertEquals(Fraction.ONE, fiveSevenths2.ipow(0));
        assertEquals(fiveSevenths2, fiveSevenths2.ipow(1));
        assertEquals(new Fraction(25, 49), fiveSevenths2.ipow(2));
        assertEquals(new Fraction(-125, 343), fiveSevenths2.ipow(3));
        assertEquals(new Fraction(-30517578125L, 4747561509943L), fiveSevenths2.ipow(15));
        // positive to negative power
        assertEquals(sevenFifths1, fiveSevenths1.ipow(-1));
        assertEquals(new Fraction(49, 25), fiveSevenths1.ipow(-2));
        assertEquals(new Fraction(343, 125), fiveSevenths1.ipow(-3));
        assertEquals(new Fraction(4747561509943L, 30517578125L), fiveSevenths1.ipow(-15));
        // negative to negative power
        assertEquals(sevenFifths2, fiveSevenths2.ipow(-1));
        assertEquals(new Fraction(49, 25), fiveSevenths2.ipow(-2));
        assertEquals(new Fraction(-343, 125), fiveSevenths2.ipow(-3));
        assertEquals(new Fraction(-4747561509943L, 30517578125L), fiveSevenths2.ipow(-15));
    }

    /**
     * Precondition of ipow(): 0^0 is undefined, so a zero fraction raised to
     * the zero power must fail the contract.
     */
    @Test
    public void testIpowZeroToZeroPower() {
        System.out.println("ipow 0^0");
        // Both the fraction and the exponent are zero: the assert must fire.
        Error ex = assertThrows(AssertionError.class, () -> {
            Fraction f = Fraction.ZERO.ipow(0);
        });
        // Check that it was this precondition, not some other one.
        assertEquals("0^0 is undefined!", ex.getMessage());
    }
    
        /**
     * Test of root method, of class Fraction.
     * Roots are computed by Newton's Method so we compare as doubles
     * within a small tolerance.
     */
    @Test
    public void testRoot() {
        System.out.println("root");
        // root(1) is the fraction itself, exactly.
        assertEquals(fiveSevenths1, fiveSevenths1.root(1));
        assertEquals(fiveSevenths2, fiveSevenths2.root(1));
        // root(-1) is the reciprocal, exactly.
        assertEquals(sevenFifths1, fiveSevenths1.root(-1));
        // Any root of zero is zero.
        assertEquals(Fraction.ZERO, Fraction.ZERO.root(2));
        assertEquals(Fraction.ZERO, Fraction.ZERO.root(3));
        // Perfect squares and cubes.
        assertEquals(2.0 / 3.0, fourNinths.root(2).doubleValue(), TOLERANCE);
        assertEquals(10.0, new Fraction(1000, 1).root(3).doubleValue(), TOLERANCE);
        assertEquals(0.5, new Fraction(1, 1024).root(10).doubleValue(), TOLERANCE);
        // Irrational result.
        assertEquals(Math.sqrt(2.0), Fraction.TWO.root(2).doubleValue(), TOLERANCE);
        // Negative root is the root of the reciprocal: (4/9)^(-1/2) = 3/2.
        assertEquals(1.5, fourNinths.root(-2).doubleValue(), TOLERANCE);
        // Odd root of a negative fraction is allowed.
        assertEquals(-2.0, new Fraction(-8, 1).root(3).doubleValue(), TOLERANCE);
        assertEquals(-0.5, new Fraction(-8, 1).root(-3).doubleValue(), TOLERANCE);
    }

    /**
     * Preconditions of root(): the root can not be zero, a negative
     * fraction can not have an even root, and zero can not have a
     * negative root.
     */
    @Test
    public void testRootPreconditions() {
        System.out.println("root preconditions");
        Error ex1 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.root(0);
        });
        assertEquals("Root can not be zero!", ex1.getMessage());
        Error ex2 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths2.root(2);
        });
        assertEquals("Even root of a negative fraction is not real!", ex2.getMessage());
        Error ex3 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths2.root(-4);
        });
        assertEquals("Even root of a negative fraction is not real!", ex3.getMessage());
        Error ex4 = assertThrows(AssertionError.class, () -> {
            Fraction f = Fraction.ZERO.root(-2);
        });
        assertEquals("Zero can not have a negative root!", ex4.getMessage());
    }
    
        /**
     * Test of pow method, of class Fraction.
     * pow(a/b) is ipow(a) followed by root(b).
     */
    @Test
    public void testPow() {
        System.out.println("pow");
        // Whole-number exponents are exact because root(1) returns the base.
        assertEquals(Fraction.ONE, fiveSevenths1.pow(Fraction.ZERO));
        assertEquals(fiveSevenths1, fiveSevenths1.pow(Fraction.ONE));
        assertEquals(new Fraction(25, 49), fiveSevenths1.pow(Fraction.TWO));
        assertEquals(new Fraction(49, 25), fiveSevenths1.pow(new Fraction(-2, 1)));
        assertEquals(Fraction.ZERO, Fraction.ZERO.pow(new Fraction(3, 2)));
        // Fractional exponents go through Newton's Method.
        assertEquals(2.0 / 3.0, fourNinths.pow(new Fraction(1, 2)).doubleValue(), TOLERANCE);
        assertEquals(8.0 / 27.0, fourNinths.pow(new Fraction(3, 2)).doubleValue(), TOLERANCE);
        assertEquals(1.5, fourNinths.pow(new Fraction(-1, 2)).doubleValue(), TOLERANCE);
        assertEquals(4.0, new Fraction(8, 1).pow(new Fraction(2, 3)).doubleValue(), TOLERANCE);
    }

    /**
     * Preconditions of pow(): the exponent can not be null, and its
     * numerator and denominator must fit in an int.  Violations inside
     * ipow() and root() (like 0^0) are also caught.
     */
    @Test
    public void testPowPreconditions() {
        System.out.println("pow preconditions");
        Error ex1 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.pow(null);
        });
        assertEquals("Exponent can not be null!", ex1.getMessage());
        Error ex2 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.pow(new Fraction(Long.MAX_VALUE, 1));
        });
        assertEquals("Exponent numerator is too large for an int!", ex2.getMessage());
        Error ex3 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths1.pow(new Fraction(1, Long.MAX_VALUE));
        });
        assertEquals("Exponent denominator is too large for an int!", ex3.getMessage());
        // 0^0 is caught by the ipow() precondition.
        Error ex4 = assertThrows(AssertionError.class, () -> {
            Fraction f = Fraction.ZERO.pow(Fraction.ZERO);
        });
        assertEquals("0^0 is undefined!", ex4.getMessage());
        // Square root of a negative is caught by the root() precondition.
        Error ex5 = assertThrows(AssertionError.class, () -> {
            Fraction f = fiveSevenths2.pow(new Fraction(1, 2));
        });
        assertEquals("Even root of a negative fraction is not real!", ex5.getMessage());
    }
    
}
