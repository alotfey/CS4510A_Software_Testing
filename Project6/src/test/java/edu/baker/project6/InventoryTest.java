package edu.baker.project6;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

/**
 * Property-based tests for Inventory.
 * Each test receives a random list of distinct chemicals, stocks them
 * with known quantities, and compares the Inventory total to a total
 * computed independently in the test.
 */
public class InventoryTest {

    /**
     * Stocks product number i with quantity i + 1 (so 1, 2, 3, ...).
     * Quantities vary, and the test can recompute them the same way.
     */
    private Inventory buildInventory(List<CleaningChemical> products) {
        Inventory inventory = new Inventory();
        for (int i = 0; i < products.size(); i++) {
            inventory.add(products.get(i), i + 1);
        }
        return inventory;
    }

    /**
     * totalCount() equals the sum of every quantity that was added.
     */
    @Property(tries = 10)
    public void testTotalCount(@ForAll("listOfProducts") List<CleaningChemical> products) {
        Inventory inventory = buildInventory(products);

        int expected = 0;
        for (int i = 0; i < products.size(); i++) {
            expected = expected + (i + 1);
        }

        assertEquals(expected, inventory.totalCount());
    }

    /**
     * totalCost() equals the sum of each chemical's cost times its quantity.
     */
    @Property(tries = 10)
    public void testTotalCost(@ForAll("listOfProducts") List<CleaningChemical> products) {
        Inventory inventory = buildInventory(products);

        double expected = 0.0;
        for (int i = 0; i < products.size(); i++) {
            expected = expected + products.get(i).getCost() * (i + 1);
        }

        double tolerance = 1e-9 * Math.max(1.0, expected);
        assertEquals(expected, inventory.totalCost(), tolerance);
    }

    /**
     * totalSizeInOunces() equals the sum of each chemical's size times its quantity.
     */
    @Property(tries = 10)
    public void testTotalSizeInOunces(@ForAll("listOfProducts") List<CleaningChemical> products) {
        Inventory inventory = buildInventory(products);

        double expected = 0.0;
        for (int i = 0; i < products.size(); i++) {
            expected = expected + products.get(i).getSizeInOunces() * (i + 1);
        }

        double tolerance = 1e-9 * Math.max(1.0, expected);
        assertEquals(expected, inventory.totalSizeInOunces(), tolerance);
    }

    /**
     * A list of 2 to 50 distinct CleaningChemical objects, built by
     * reusing the single-object provider from CleaningChemicalTest.
     */
    @Provide
    public Arbitrary<List<CleaningChemical>> listOfProducts() {
        Arbitrary<CleaningChemical> oneChemical = new CleaningChemicalTest().cleaningChemicalProvider();
        return oneChemical.list().ofMinSize(2).ofMaxSize(50).uniqueElements();
    }

    /**
     * Adding the same chemical twice merges into one entry with the summed count.
     * This path is not reachable from the property tests because
     * listOfProducts() guarantees unique elements.
     */
    @Test
    public void testAddSameChemicalTwice() {
        CleaningChemical bleach = new CleaningChemical("Bleach", "Clorox", "1234567890123", 3.49, 64.0);
        Inventory inventory = new Inventory();
        inventory.add(bleach, 2);
        inventory.add(bleach, 3);
        assertEquals(5, inventory.totalCount());
    }
}