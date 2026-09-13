package edu.baker.project6;

import static org.junit.jupiter.api.Assertions.*;

import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

/**
 * Property-based tests for CleaningChemical.
 */
public class CleaningChemicalTest {

    /**
     * getCostPerOunce() must be consistent with the cost and size it
     * was built from: cost per ounce times ounces gives the cost back,
     * and it is never negative because cost is never negative.
     */
    @Property(tries = 100)
    public void testCostPerOunce(
            @ForAll("cleaningChemicalProvider") CleaningChemical chemical) {
        double costPerOunce = chemical.getCostPerOunce();

        assertTrue(costPerOunce >= 0);

        double rebuiltCost = costPerOunce * chemical.getSizeInOunces();
        double tolerance = 1e-9 * Math.max(1.0, chemical.getCost());
        assertEquals(chemical.getCost(), rebuiltCost, tolerance);
    }

    /**
     * Teaches jqwik how to build a random CleaningChemical.
     * Every generator is constrained so the values satisfy the
     * constructor's preconditions; otherwise the assert would fire
     * during generation, before the test even runs.
     */
    @Provide
    public Arbitrary<CleaningChemical> cleaningChemicalProvider() {
        Arbitrary<String> names = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20);
        Arbitrary<String> manufacturers = Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(20);
        Arbitrary<String> productCodes = Arbitraries.strings().numeric().ofLength(13);
        Arbitrary<Double> costs = Arbitraries.doubles().between(0.0, 100.0);
        Arbitrary<Double> sizes = Arbitraries.doubles().between(0.1, 256.0);

        return Combinators.combine(names, manufacturers, productCodes, costs, sizes)
                .as((name, manufacturer, productCode, cost, size)
                        -> new CleaningChemical(name, manufacturer, productCode, cost, size));
    }



    /**
     * The three getters the property test never touches, and the
     * toString() format required by the assignment.
     */
    @Test
    public void testGettersAndToString() {
        CleaningChemical bleach = new CleaningChemical("Bleach", "Clorox", "1234567890123", 1.0, 32.0);
        assertEquals("Bleach", bleach.getName());
        assertEquals("Clorox", bleach.getManufacturer());
        assertEquals("1234567890123", bleach.getProductCode());
        assertEquals("CleaningChemical{name=Bleach, manufacturer=Clorox, productCode=1234567890123, cost=1.00, sizeInOunces=32.0}",
                bleach.toString());
    }

    /**
     * The equals()/hashCode() contract that HashMap and uniqueElements() rely on:
     * same fields means equal and same hash; an object equals itself;
     * nothing equals null or a different type; one different field breaks equality.
     */
    @Test
    public void testEqualsAndHashCode() {
        CleaningChemical a = new CleaningChemical("Bleach", "Clorox", "1234567890123", 1.0, 32.0);
        CleaningChemical b = new CleaningChemical("Bleach", "Clorox", "1234567890123", 1.0, 32.0);
        CleaningChemical c = new CleaningChemical("Bleach", "Clorox", "1234567890124", 1.0, 32.0);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "not a chemical");
        assertNotEquals(a, c);
    }
}