package edu.baker.project5;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Project5.
 * Example-based tests use JUnit 5 (@Test).
 * Property-based tests use jqwik (@Property), added in later steps.
 */
public class Project5Test {

    /**
     * Test of sign method, of class Project5.
     * Zero is the one value that is neither negative nor positive,
     * so it is tested as a single example rather than a property.
     */
    @Test
    public void testSign() {
        assertEquals(0, Project5.sign(0));
    }

    /**
     * Test of canRetire method, of class Project5.
     * Exact boundary pairs where each rule switches on or off.
     */
    @Test
    public void testCanRetire() {
        assertTrue(Project5.canRetire(72, 0));     // age rule, exactly 72
        assertFalse(Project5.canRetire(71, 0));    // one year short
        assertTrue(Project5.canRetire(30, 25));    // service rule, exactly 25
        assertFalse(Project5.canRetire(30, 24));   // one year short
        assertTrue(Project5.canRetire(65, 15));    // combined rule, both exact
        assertFalse(Project5.canRetire(64, 15));   // age short
        assertFalse(Project5.canRetire(65, 14));   // service short
    }

    /**
     * Test of marginalSingleTaxRate method, of class Project5.
     * Every bracket start, and one cent below it.
     */
    @Test
    public void testMarginalSingleTaxRate() {
        assertEquals(0.10, Project5.marginalSingleTaxRate(0.0));
        assertEquals(0.10, Project5.marginalSingleTaxRate(10275.99));
        assertEquals(0.12, Project5.marginalSingleTaxRate(10276.0));
        assertEquals(0.22, Project5.marginalSingleTaxRate(41776.0));
        assertEquals(0.24, Project5.marginalSingleTaxRate(89076.0));
        assertEquals(0.32, Project5.marginalSingleTaxRate(170051.0));
        assertEquals(0.35, Project5.marginalSingleTaxRate(215951.0));
        assertEquals(0.35, Project5.marginalSingleTaxRate(539900.99));
        assertEquals(0.37, Project5.marginalSingleTaxRate(539901.0));
    }

    /**
     * Test of sign method negative property, of class Project5.
     */
    @Property(tries = 1000)
    public void testSignNegative(
            @ForAll
            @IntRange(min = -32767, max = -1)
            int x) {
        assertEquals(-1, Project5.sign(x));
    }

    /**
     * Test of sign method positive property, of class Project5.
     */
    @Property(tries = 1000)
    public void testSignPositive(
            @ForAll
            @IntRange(min = 1, max = 32767)
            int x) {
        assertEquals(1, Project5.sign(x));
    }

    /**
     * Rule 1: age 72 or older can retire, regardless of years of service.
     * Years are kept below 15 so neither of the other two rules can be
     * the reason the answer is true.
     */
    @Property(tries = 1000)
    public void testCanRetireAge(
            @ForAll @IntRange(min = 72, max = 120) int age,
            @ForAll @IntRange(min = 0, max = 14) int yearsOfService) {
        assertTrue(Project5.canRetire(age, yearsOfService));
    }

    /**
     * Rule 2: 25 or more years of service can retire, regardless of age.
     * Age is kept below 65 so neither of the other two rules applies.
     */
    @Property(tries = 1000)
    public void testCanRetireYearsOfService(
            @ForAll @IntRange(min = 18, max = 64) int age,
            @ForAll @IntRange(min = 25, max = 60) int yearsOfService) {
        assertTrue(Project5.canRetire(age, yearsOfService));
    }

    /**
     * Rule 3: age 65 to 71 with 15 to 24 years of service can retire.
     * Both ranges stop just short of the other two rules (72 and 25),
     * so this is the only rule that can make the answer true.
     */
    @Property(tries = 1000)
    public void testCanRetireBoth(
            @ForAll @IntRange(min = 65, max = 71) int age,
            @ForAll @IntRange(min = 15, max = 24) int yearsOfService) {
        assertTrue(Project5.canRetire(age, yearsOfService));
    }

    /**
     * No rule applies: under 72, under 25 years, and not (65+ with 15+).
     * Assume.that() throws away the generated pairs that would satisfy
     * rule 3, so every pair that reaches the assertion must be false.
     */
    @Property(tries = 1000)
    public void testCanRetireNegative(
            @ForAll @IntRange(min = 18, max = 71) int age,
            @ForAll @IntRange(min = 0, max = 24) int yearsOfService) {
        Assume.that(age < 65 || yearsOfService < 15);
        assertFalse(Project5.canRetire(age, yearsOfService));
    }

    /**
     * Bottom bracket: 0 up to but not including 10276 is taxed at 10%.
     */
    @Property(tries = 1000)
    public void testMarginalSingleTaxRateBottomBracket(
            @ForAll @DoubleRange(min = 0.0, max = 10276.0, maxIncluded = false) double income) {
        assertEquals(0.10, Project5.marginalSingleTaxRate(income));
    }

    /**
     * Middle bracket: 89076 up to but not including 170051 is taxed at 24%.
     */
    @Property(tries = 1000)
    public void testMarginalSingleTaxRateMiddleBracket(
            @ForAll @DoubleRange(min = 89076.0, max = 170051.0, maxIncluded = false) double income) {
        assertEquals(0.24, Project5.marginalSingleTaxRate(income));
    }

    /**
     * Top bracket: 539901 and above is taxed at 37%, with no upper limit.
     */
    @Property(tries = 1000)
    public void testMarginalSingleTaxRateTopBracket(
            @ForAll @DoubleRange(min = 539901.0, max = 10000000.0) double income) {
        assertEquals(0.37, Project5.marginalSingleTaxRate(income));
    }

    /**
     * The rate never decreases as income increases.
     * Two incomes are generated; the lower one must not have a higher rate.
     */
    @Property(tries = 1000)
    public void testMarginalSingleTaxRateNeverDecreases(
            @ForAll @DoubleRange(min = 0.0, max = 1000000.0) double a,
            @ForAll @DoubleRange(min = 0.0, max = 1000000.0) double b) {
        double lowerIncome = Math.min(a, b);
        double higherIncome = Math.max(a, b);
        double lowerRate = Project5.marginalSingleTaxRate(lowerIncome);
        double higherRate = Project5.marginalSingleTaxRate(higherIncome);
        assertTrue(lowerRate <= higherRate);
    }

    /**
     * Every non-negative income gets one of the real rates, 10% to 37%.
     */
    @Property(tries = 1000)
    public void testMarginalSingleTaxRateAlwaysValid(
            @ForAll @DoubleRange(min = 0.0, max = 10000000.0) double income) {
        double rate = Project5.marginalSingleTaxRate(income);
        assertTrue(rate >= 0.10 && rate <= 0.37);
    }

//    Step 16
    /**
     * sinh from the exponential formula must agree with Math.sinh, the oracle.
     * Range stops at +/-700: at 710, exp(710) overflows to Infinity while
     * Math.sinh(710) is still finite (about 1.1e308).
     * Tolerance is absolute (1e-9) for small results and relative (1e-9 of
     * the expected value) for large ones, because subtracting e^x and e^-x
     * loses relative precision near zero and absolute precision far from it.
     */
    @Property(tries = 1000)
    public void testSinh(
            @ForAll @DoubleRange(min = -700.0, max = 700.0) double x) {
        double expected = Math.sinh(x);
        double actual = Project5.sinh(x);
        double tolerance = 1e-9 * Math.max(1.0, Math.abs(expected));
        assertEquals(expected, actual, tolerance);
    }

    /**
     * sinh is an odd function: sinh(-x) == -sinh(x).
     * This property needs no oracle; it checks the function against itself.
     */
    @Property(tries = 1000)
    public void testSinhIsOdd(
            @ForAll @DoubleRange(min = -700.0, max = 700.0) double x) {
        assertEquals(-Project5.sinh(x), Project5.sinh(-x), 1e-12);
    }

    /**
     * sinh is increasing: a larger input never gives a smaller output.
     */
    @Property(tries = 1000)
    public void testSinhIsIncreasing(
            @ForAll @DoubleRange(min = -700.0, max = 700.0) double a,
            @ForAll @DoubleRange(min = -700.0, max = 700.0) double b) {
        double lower = Math.min(a, b);
        double higher = Math.max(a, b);
        assertTrue(Project5.sinh(lower) <= Project5.sinh(higher));
    }

    /**
     * Test of sinh method, of class Project5. The one exact value: sinh(0) = 0.
     */
    @Test
    public void testSinhZero() {
        assertEquals(0.0, Project5.sinh(0.0));
    }
}