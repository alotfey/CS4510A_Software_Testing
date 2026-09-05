package edu.baker.project3;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Class that implements a fraction class using arbitrary precision arithmetic.
 * @author Richard Lesh
 */
public class Fraction {
    private BigInteger num, denom;

    public final static Fraction ZERO = new Fraction(0,1);
    public final static Fraction ONE = new Fraction(1,1);
    
    public Fraction(long n, long d) throws ArithmeticException {
        // Denominator can never be zero.
        if (d == 0L)
            throw new ArithmeticException("Denominator can not be zero!");
        // Denominator should always be positive.
        if (d < 0) {
            n = -n;
            d = -d;
        }
        num = BigInteger.valueOf(n);
        denom = BigInteger.valueOf(d);
        this.reduce();
    }
    
    public Fraction(BigInteger n, BigInteger d) throws ArithmeticException {
        // Denominator can never be zero.
       if (d.signum() == 0)
            throw new ArithmeticException("Denominator can not be zero!");
        // Denominator should always be positive.
        if (d.signum() < 0) {
            n = n.negate();
            d = d.negate();
        }
        num = n;
        denom = d;
        this.reduce();
    }
    
    @Override
    public String toString() {
        return num.toString() + "/" + denom.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(num, denom);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Fraction)) return false;
        Fraction fo = (Fraction)o;
        return num.equals(fo.num) && denom.equals(fo.denom);
    }
    
    /**
     * Reduces the fraction to simplest terms, i.e. numerator
     * and denominator will have no common factor after the method.
     */
    public final void reduce() {
        if (num.signum() == 0) {
            denom = BigInteger.ONE;
        } else {
            BigInteger gcd = num.gcd(denom);
            if (!gcd.equals(BigInteger.ONE)) {
                num = num.divide(gcd);
                denom = denom.divide(gcd);
            }
        }
    }
    
    /**
     * Adds the argument to the fraction.
     * @param x Value to add to the fraction
     * @return Sum of the fraction and the argument.
     */
    public final Fraction add(Fraction x) {
        return new Fraction(num.multiply(x.denom).add(x.num.multiply(denom)), denom.multiply(x.denom));
    }
    
    /**
     * Subtracts the argument from the fraction.
     * @param x Value to subtract from the fraction
     * @return Difference of the fraction and the argument.
     */
    public final Fraction sub(Fraction x) {
        return new Fraction(num.multiply(x.denom).subtract(x.num.multiply(denom)), denom.multiply(x.denom));
    }
    
    /**
     * Multiplies the fraction by the argument.
     * @param x Value to multiply the fraction by
     * @return Product of the fraction and the argument.
     */
    public final Fraction mult(Fraction x) {
        return new Fraction(num.multiply(x.num), denom.multiply(x.denom));
    }
    
    /**
     * Divides the fraction by the argument.
     * @param x Value to divide the fraction by
     * @return Quotient of the fraction and the argument.
     */
    public final Fraction div(Fraction x) {
        return new Fraction(num.multiply(x.denom), denom.multiply(x.num));
    }
    
    /**
     * Computes the reciprocal of the fraction.
     * @return Reciprocal of the fraction
     * @throws ArithmeticException if denominator of the result is zero.
     */
    public final Fraction reciprocal() throws ArithmeticException {
        return new Fraction(denom, num);
    }
    
    /**
     * Compute the negation of the fraction.
     * @return Fraction times -1.
     */
    public final Fraction negate() {
        return new Fraction(num.negate(), denom);
    }
    
    /**
     * Returns the sign of the fraction.
     * @return -1 if negative, 0 if 0 and +1 if positive.
     */
    public final int sign() {
        return num.signum();
    }
    
    /**
     * Computes the absolute value of the fraction.
     * @return The fraction if positive or zero,
     *          the fraction times -1 if negative.
     */
    public final Fraction abs() {
        if (sign() < 0) {
            return new Fraction(num.negate(), denom);
        } else {
            return this;
        }
    }
    
    /**
     * Compares the fraction to the argument.
     * @param f Argument to compare to the fraction
     * @return -1 if fraction < arg, 0 if equal and +1 if fraction > arg.
     */
    public final int compare(Fraction f) {
        return this.sub(f).sign();
    }
    
    /**
     * Computes the fraction raised to an integer power.
     * @param n Power to raise the fraction to.  Can be positive, negative or zero.
     * @return Fraction raised to the argument power.
     * @throws ArithmeticException If fraction numerator is zero and power is negative.
     */
    public final Fraction ipow(int n) throws ArithmeticException {
        Fraction base = this;
        if (n < 0) {
            base = this.reciprocal();
            n = -n;
        }
        if (base.sign() == 0) return base;
        Fraction result = new Fraction(1, 1);
        
        Fraction square = base;
        while (n > 0) {
            if (n % 2 == 1) {
                result = result.mult(square);
            }
            square = square.mult(square);
            n >>>= 1;
        }
        return result;
    }
}
