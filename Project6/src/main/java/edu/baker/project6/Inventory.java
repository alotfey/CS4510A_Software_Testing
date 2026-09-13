package edu.baker.project6;

import java.util.HashMap;
import java.util.Map;

/**
 * Stock on hand for a set of cleaning chemicals.
 * Each chemical maps to how many units of it are in stock.
 */
public class Inventory {
    private Map<CleaningChemical, Integer> inventory;

    public Inventory() {
        inventory = new HashMap<>();
    }

    /**
     * Adds quantity units of a chemical. If the chemical is already
     * stocked, the quantity is added to the existing count.
     * Precondition: chemical is not null and quantity is 0 or greater.
     */
    public void add(CleaningChemical chemical, int quantity) {
        assert chemical != null : "chemical can not be null";
        assert quantity >= 0 : "quantity must be 0 or greater";

        int current = 0;
        if (inventory.containsKey(chemical)) {
            current = inventory.get(chemical);
        }
        inventory.put(chemical, current + quantity);
    }

    /**
     * Total number of units in stock, across all chemicals.
     */
    public int totalCount() {
        int total = 0;
        for (int count : inventory.values()) {
            total = total + count;
        }
        return total;
    }

    /**
     * Total cost of everything in stock: each chemical's cost
     * times how many units of it are stocked.
     */
    public double totalCost() {
        double total = 0.0;
        for (CleaningChemical chemical : inventory.keySet()) {
            int count = inventory.get(chemical);
            total = total + chemical.getCost() * count;
        }
        return total;
    }

    /**
     * Total ounces of product in stock: each chemical's size
     * times how many units of it are stocked.
     */
    public double totalSizeInOunces() {
        double total = 0.0;
        for (CleaningChemical chemical : inventory.keySet()) {
            int count = inventory.get(chemical);
            total = total + chemical.getSizeInOunces() * count;
        }
        return total;
    }
}