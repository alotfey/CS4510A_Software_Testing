package edu.baker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project 11 for CS4510 Assignment 11.
 * Holds a Fibonacci function and a McCarthy 91 function, each built one
 * requirement at a time using Test-Driven Development.
 */
public class Project11 {

    /**
     * The largest input fibonacci() accepts. fibonacci(92) is the largest
     * Fibonacci number that fits in a long; fibonacci(93) overflows. This
     * value was found with the loop in main().
     */
    public static final int MAX_FIBONACCI_INPUT = 92;
    /**
     * Cache of Fibonacci results that have already been computed.
     * The value at index n is fibonacci(n). It starts with the two base
     * cases, 0 for input 0 and 1 for input 1.
     */
    private static final List<Long> fibonacciValues = new ArrayList<>(Arrays.asList(0L, 1L));

    /**
     * Program entry point. Prints fibonacci() for every input from 1 to
     * MAX_FIBONACCI_INPUT. This loop was first run up to 200 to find where a
     * long overflows (input 93 returned a negative number), which is how
     * MAX_FIBONACCI_INPUT was set to 92.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        for (int i = 1; i <= MAX_FIBONACCI_INPUT; i++) {
            System.out.println(i + " -> " + fibonacci(i));
        }
        System.out.println("Largest safe input: " + MAX_FIBONACCI_INPUT);
    }

    /**
     * Computes the Fibonacci number for the given input, using a cache so each
     * value is only computed once. Inputs below 0 or above MAX_FIBONACCI_INPUT
     * are rejected. If the answer is already in fibonacciValues it is returned
     * directly; otherwise it is computed as fibonacci(n - 1) + fibonacci(n - 2),
     * added to the cache, and returned.
     *
     * @param n the position in the Fibonacci sequence, from 0 to MAX_FIBONACCI_INPUT
     * @return the Fibonacci number at position n
     * @throws IllegalArgumentException if n is negative or greater than MAX_FIBONACCI_INPUT
     */
    public static long fibonacci(int n) {
        if (n < 0 || n > MAX_FIBONACCI_INPUT) {
            throw new IllegalArgumentException("fibonacci input must be 0 - " + MAX_FIBONACCI_INPUT);
        }
        if (n < fibonacciValues.size()) {
            return fibonacciValues.get(n);
        }
        long result = fibonacci(n - 1) + fibonacci(n - 2);
        fibonacciValues.add(result);
        return result;
    }

    /**
     * Computes the McCarthy 91 function for the given input.
     * Inputs greater than 100 return the input minus 10. Inputs of 100 or
     * less return mcCarthy91(mcCarthy91(n + 11)), which always works out to 91.
     *
     * @param n the input value
     * @return n - 10 when n is greater than 100, otherwise 91
     */
    public static int mcCarthy91(int n) {
        if (n > 100) {
            return n - 10;
        }
        return mcCarthy91(mcCarthy91(n + 11));
    }
}