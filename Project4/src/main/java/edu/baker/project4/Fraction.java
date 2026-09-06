package edu.baker.project4;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

/**
 * Class that implements a fraction class using arbitrary precision arithmetic.
 *
 * @author Richard Lesh
 */
public class Fraction {

    private BigInteger num, denom;

    public final static Fraction ZERO = new Fraction(0, 1);
    public final static Fraction ONE = new Fraction(1, 1);
    public final static Fraction TWO = new Fraction(2, 1);
    public final static Fraction TEN = new Fraction(10, 1);

    public Fraction(long n, long d)  {
        // Precondition: denominator can never be zero.
        assert d != 0L : "Denominator can not be zero!";
        // Denominator should always be positive.
        if (d < 0) {
            n = -n;
            d = -d;
        }
        num = BigInteger.valueOf(n);
        denom = BigInteger.valueOf(d);
        this.reduce();
        // Postcondition: the new Fraction satisfies the class invariant.
        assert isValid() : "Constructed Fraction is invalid!";
    }

    public Fraction(BigInteger n, BigInteger d) {
        // Precondition: denominator can never be zero.
        assert d.signum() != 0 : "Denominator can not be zero!";
        // Denominator should always be positive.
        if (d.signum() < 0) {
            n = n.negate();
            d = d.negate();
        }
        num = n;
        denom = d;
        this.reduce();
        // Postcondition: the new Fraction satisfies the class invariant.
        assert isValid() : "Constructed Fraction is invalid!";
    }

    /**
     * Class invariant check.  A Fraction is valid when its denominator is
     * not zero and, if the numerator is zero, the denominator is exactly one
     * (because reduce() always stores zero as 0/1).
     * @return true if the Fraction is in a valid state, false otherwise.
     */
    public final boolean isValid() {
        // Invalid: numerator is zero but denominator is not 1 (zero must be 0/1).
        if (num.signum() == 0 && !denom.equals(BigInteger.ONE))
            return false;
        // Invalid: denominator is zero.
        if (denom.signum() == 0)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return num.toString() + "/" + denom.toString();
    }

    /**
     * Converts the fraction to the closest double value.
     * @return The fraction as a double.
     */
    public double doubleValue() {
        // Precondition: this Fraction must be valid (no zero denominator).
        assert isValid() : "Fraction is invalid!";
        BigDecimal n = new BigDecimal(num);
        BigDecimal d = new BigDecimal(denom);
        return n.divide(d, MathContext.DECIMAL128).doubleValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(num, denom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Fraction)) {
            return false;
        }
        Fraction fo = (Fraction) o;
        return num.equals(fo.num) && denom.equals(fo.denom);
    }

    /**
     * Reduces the fraction to simplest terms, i.e. numerator and denominator
     * will have no common factor after the method. Also reduces the number of
     * bits used if the numerator and denominator get too large.
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
            int numExcessBits = Math.max(num.bitCount() - 256, 0);
            int denomExcessBits = Math.max(denom.bitCount() - 256, 0);
            int removeBits = Math.min(numExcessBits, denomExcessBits);
            if (removeBits > 0) {
                num = num.shiftRight(removeBits);
                denom = denom.shiftRight(removeBits);
            }
        }
    }

    /**
     * Adds the argument to the fraction.
     *
     * @param x Value to add to the fraction
     * @return Sum of the fraction and the argument.
     */
    public final Fraction add(Fraction x) {
        // Preconditions: this Fraction and the argument must both be valid.
        assert isValid() : "Fraction is invalid!";
        assert x != null : "Argument can not be null!";
        assert x.isValid() : "Argument Fraction is invalid!";
        return new Fraction(num.multiply(x.denom).add(x.num.multiply(denom)), denom.multiply(x.denom));
    }

    /**
     * Subtracts the argument from the fraction.
     *
     * @param x Value to subtract from the fraction
     * @return Difference of the fraction and the argument.
     */
    public final Fraction sub(Fraction x) {
        // Preconditions: this Fraction and the argument must both be valid.
        assert isValid() : "Fraction is invalid!";
        assert x != null : "Argument can not be null!";
        assert x.isValid() : "Argument Fraction is invalid!";
        return new Fraction(num.multiply(x.denom).subtract(x.num.multiply(denom)), denom.multiply(x.denom));
    }

    /**
     * Multiplies the fraction by the argument.
     *
     * @param x Value to multiply the fraction by
     * @return Product of the fraction and the argument.
     */
    public final Fraction mult(Fraction x) {
        // Preconditions: this Fraction and the argument must both be valid.
        assert isValid() : "Fraction is invalid!";
        assert x != null : "Argument can not be null!";
        assert x.isValid() : "Argument Fraction is invalid!";
        return new Fraction(num.multiply(x.num), denom.multiply(x.denom));
    }

    /**
     * Divides the fraction by the argument.
     *
     * @param x Value to divide the fraction by
     * @return Quotient of the fraction and the argument.
     */
    public final Fraction div(Fraction x) {
        // Preconditions: this Fraction and the argument must both be valid.
        assert isValid() : "Fraction is invalid!";
        assert x != null : "Argument can not be null!";
        assert x.isValid() : "Argument Fraction is invalid!";
        return new Fraction(num.multiply(x.denom), denom.multiply(x.num));
    }

    /**
     * Computes the reciprocal of the fraction.
     *
     * @return Reciprocal of the fraction
     * @throws ArithmeticException if denominator of the result is zero.
     */
    public final Fraction reciprocal() {
        // Precondition: this Fraction must be valid.
        assert isValid() : "Fraction is invalid!";
        return new Fraction(denom, num);
    }

    /**
     * Compute the negation of the fraction.
     *
     * @return Fraction times -1.
     */
    public final Fraction negate() {
        // Precondition: this Fraction must be valid.
        assert isValid() : "Fraction is invalid!";
        return new Fraction(num.negate(), denom);
    }

    /**
     * Returns the sign of the fraction.
     *
     * @return -1 if negative, 0 if 0 and +1 if positive.
     */
    public final int sign() {
        assert isValid() : "Fraction is invalid!";
        return num.signum();
    }

    /**
     * Computes the absolute value of the fraction.
     *
     * @return The fraction if positive or zero, the fraction times -1 if
     * negative.
     */
    public final Fraction abs() {
        // Precondition: this Fraction must be valid.
        assert isValid() : "Fraction is invalid!";
        if (sign() < 0) {
            return new Fraction(num.negate(), denom);
        } else {
            return this;
        }
    }

    /**
     * Compares the fraction to the argument.
     *
     * @param f Argument to compare to the fraction
     * @return -1 if fraction < arg, 0 if equal and +1 if fraction > arg.
     */
    public final int compare(Fraction f) {
        // Preconditions: this Fraction and the argument must both be valid.
        assert isValid() : "Fraction is invalid!";
        assert f != null : "Argument can not be null!";
        assert f.isValid() : "Argument Fraction is invalid!";
        return this.sub(f).sign();
    }

    /**
     * Computes the fraction raised to an integer power.
     * The fraction and the power can not both be zero (0^0 is undefined).
     * @param n Power to raise the fraction to.  Can be positive, negative or zero.
     * @return Fraction raised to the argument power.
     */
    public final Fraction ipow(int n) {
        // Preconditions: this Fraction must be valid, and 0^0 is undefined.
        assert isValid() : "Fraction is invalid!";
        assert !(num.signum() == 0 && n == 0) : "0^0 is undefined!";
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

    /**
     * Computes the nth root of a fraction using Newton's Method.
     * The root can not be zero.  A negative fraction can only have an
     * odd root (an even root of a negative number is not a real number).
     * A zero fraction can not have a negative root (division by zero).
     * @param n Root to take.  Can be positive, negative but not zero.
     * @return Nth root of the fraction.
     */
    public final Fraction root(int n) {
        // Preconditions
        assert isValid() : "Fraction is invalid!";
        assert n != 0 : "Root can not be zero!";
        assert !(num.signum() < 0 && n % 2 == 0) : "Even root of a negative fraction is not real!";
        assert !(num.signum() == 0 && n < 0) : "Zero can not have a negative root!";
        Fraction base = this;
        if (n < 0) {
            base = this.reciprocal();
            n = -n;
        }
        Fraction nRecip = new Fraction(1, n);
        if (n == 1) {
            return base;
        }
        if (base.sign() == 0) {
            return ZERO;
        }
        Fraction result = base;
        Fraction epsilon = TEN.ipow(-8);
        Fraction term;
        do {
            term = nRecip.mult(base.div(result.ipow(n - 1)).sub(result));
            result = result.add(term);
        } while (term.div(result).abs().compare(epsilon) > 0);
        return result;
    }

    /**
     * Computes the fraction raised to a fraction power.
     * The exponent's numerator and denominator must each fit in an int
     * because the work is done by ipow(int) and root(int).
     * @param n Power to raise the fraction to.  Can be positive, negative or zero.
     * @return Fraction raised to the argument power.
     */
    public final Fraction pow(Fraction n) {
        // Preconditions
        assert isValid() : "Fraction is invalid!";
        assert n != null : "Exponent can not be null!";
        assert n.isValid() : "Exponent Fraction is invalid!";
        assert n.num.bitLength() < 32 : "Exponent numerator is too large for an int!";
        assert n.denom.bitLength() < 32 : "Exponent denominator is too large for an int!";
        return ipow(n.num.intValueExact()).root(n.denom.intValueExact());
    }
}
