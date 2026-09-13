package edu.baker.project6;

import java.util.Objects;

/**
 * A cleaning chemical product carried in inventory.
 * All fields are set once in the constructor and never change.
 */
public class CleaningChemical {
    private String name;
    private String manufacturer;
    private String productCode;
    private double cost;
    private double sizeInOunces;

    /**
     * Preconditions (Design by Contract, Module 2):
     *   name, manufacturer and productCode are not null and not blank
     *   productCode is exactly 13 characters
     *   cost is 0 or greater
     *   sizeInOunces is greater than 0
     */
    public CleaningChemical(String name, String manufacturer, String productCode,
                            double cost, double sizeInOunces) {
        assert name != null && !name.isBlank() : "name can not be null or blank";
        assert manufacturer != null && !manufacturer.isBlank() : "manufacturer can not be null or blank";
        assert productCode != null && !productCode.isBlank() : "productCode can not be null or blank";
        assert productCode.length() == 13 : "productCode must be exactly 13 characters";
        assert cost >= 0 : "cost must be 0 or greater";
        assert sizeInOunces > 0 : "sizeInOunces must be greater than 0";

        this.name = name;
        this.manufacturer = manufacturer;
        this.productCode = productCode;
        this.cost = cost;
        this.sizeInOunces = sizeInOunces;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProductCode() {
        return productCode;
    }

    public double getCost() {
        return cost;
    }

    public double getSizeInOunces() {
        return sizeInOunces;
    }

    /**
     * Cost of one ounce of product. Safe to divide because the
     * constructor guarantees sizeInOunces > 0.
     */
    public double getCostPerOunce() {
        return cost / sizeInOunces;
    }

    @Override
    public String toString() {
        return "CleaningChemical{name=" + name
                + ", manufacturer=" + manufacturer
                + ", productCode=" + productCode
                + ", cost=" + String.format("%.2f", cost)
                + ", sizeInOunces=" + sizeInOunces + "}";
    }

    /**
     * Two chemicals are equal when all five fields are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CleaningChemical other = (CleaningChemical) obj;
        return Double.compare(cost, other.cost) == 0
                && Double.compare(sizeInOunces, other.sizeInOunces) == 0
                && name.equals(other.name)
                && manufacturer.equals(other.manufacturer)
                && productCode.equals(other.productCode);
    }

    /**
     * Built from the same five fields as equals(), so equal objects
     * always get the same hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturer, productCode, cost, sizeInOunces);
    }
}