/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package edu.baker.project5;

/**
 * Simple class with static methods to test using property-based testing.
 * @author Richard Lesh
 */
public class Project5 {
    /**
     * Returns the sign of the argument.
     * @returns -1 if negative, 0 for 0 and +1 if positive
     */
    public static int sign(int x) {
        return x < 0 ? -1 : x > 0 ? 1 : 0;
    }
    
    /**
     * Determines if an employee can retire or not.
     * Employees can retire with pension if they:
     *  Have 25 or more years of service
     *  Are age 72 or older
     *  are age 65 or older with 15 or more years of service
     * @param age The age of the employee
     * @param yearsOfService The employee's years of service
     * @return True if they can retire with pension, false if they can not
     */
    public static boolean canRetire(int age, int yearsOfService) {
        if (age >= 72) return true;
        if (yearsOfService >= 25) return true;
        if (age >= 65 && yearsOfService >= 15) return true;
        return false;
    }
    
    private static final double[] singleTaxpayerBracketStart = {
        0., 10276., 41776., 89076., 170051., 215951., 539901.};
    private static final double[] marginalRates = {
        0.10, 0.12, 0.22, 0.24, 0.32, 0.35, 0.37};
    /**
     * Returns the marginal federal tax rate for a single taxpayer.
     * https://www.irs.com/articles/2022-federal-income-tax-brackets-rates-standard-deductions
     * @param taxableIncome Total taxable income for the year.
     * @return Marginal tax rate corresponding to income.
     */
    public static double marginalSingleTaxRate(double taxableIncome) {
        double marginalRate = 0.;
        for (int i = 0; i < marginalRates.length; ++i) {
            if (taxableIncome >= singleTaxpayerBracketStart[i])
                marginalRate = marginalRates[i];
            else
                break;
        }
        return marginalRate;
    }

    /**
     * Hyperbolic sine, computed from its exponential definition:
     *     sinh(x) = (e^x - e^-x) / 2
     * https://en.wikipedia.org/wiki/Hyperbolic_functions#Exponential_definitions
     * @param x the argument
     * @return the hyperbolic sine of x
     */
    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x)) / 2.0;
    }
    
    public static void main(String[] args) {
        System.out.println("Project 5!");
    }
}
