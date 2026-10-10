package edu.baker.project13;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Class that models an RPN scientific calculator.
 * @author Richard Lesh
 */
public class RPNCalculator {
    private final Deque<Double> stack = new ArrayDeque<>();

    public RPNCalculator() {}
    
    public boolean isEmpty() {return stack.isEmpty();}
    public double peek() {return stack.peek();}
    public double peekY() {swap();double y = stack.peek();swap();return y;}
    public void swap() {var x = stack.pop(); var y = stack.pop(); value(x); value(y);}
    public void rollDown() {stack.pop();}
    public void clear() {stack.clear();}
    public void value(double v) {stack.push(v);}
    public void add() {value(stack.pop() + stack.pop());}
    public void sub() {var x = stack.pop(); var y = stack.pop();value(y - x);}
    public void mult() {value(stack.pop() * stack.pop());}
    public void div() {var x = stack.pop(); var y = stack.pop();value(y / x);}
    public void min() {var x = stack.pop(); var y = stack.pop(); value(Math.min(x,y));}
    public void max() {var x = stack.pop(); var y = stack.pop(); value(Math.max(x,y));}
    public void abs() {value(Math.abs(stack.pop()));}
    public void sign() {value(Math.signum(stack.pop()));}
    public void recip() {value(1./stack.pop());}
    public void neg() {value(-stack.pop());}
    public void floor() {value(Math.floor(stack.pop()));}
    public void ceil() {value(Math.ceil(stack.pop()));}
    public void round() {
        double x = stack.pop();
        if (x < 0) {
            value(-Math.round(-x));
        } else {
            value(Math.round(x));
        }
    }
    public void trunc() {
            var x = stack.pop();
            if (x < 0) value(Math.ceil(x));
            else value(Math.floor(x));
    }
    public void ipow() {var x = stack.pop(); var y = stack.pop(); value(ipow(y,x.longValue()));}
    private static double ipow(double base, long ipower) {
        if (base == 0) {
            if (ipower == 0)
                throw new IllegalArgumentException("ipow(0,0) is indeterminate!");
            else return 0.0;
        }
        if (ipower == 0) return 1.0;
        double result = 1.0;
        double factor = base;
        while (ipower != 0) {
            if ((ipower & 1L) == 1L)
                result = result * factor;
            ipower >>= 1;
            factor = factor * factor;
        }
        return result;
    }
    public void pow() {var x = stack.pop(); var y = stack.pop(); value(Math.pow(y,x));}
    public void root() {var x = stack.pop(); var y = stack.pop(); value(Math.pow(y,1./x));}
    public void exp() {value(Math.exp(stack.pop()));}
    public void log() {value(Math.log(stack.pop()));}
    public void exp10() {value(Math.pow(10.,stack.pop()));}
    public void log10() {value(Math.log10(stack.pop()));}
    public void cos() {value(Math.cos(stack.pop()));}
    public void sin() {value(Math.sin(stack.pop()));}
    public void tan() {value(Math.tan(stack.pop()));}
    public void acos() {value(Math.acos(stack.pop()));}
    public void asin() {value(Math.asin(stack.pop()));}
    public void atan() {value(Math.atan(stack.pop()));}
    public void atan2() {var x = stack.pop(); var y = stack.pop(); value(Math.atan2(y,x));}
    public void e() {value(Math.E);}
    public void π() {value(Math.PI);}
    public void x() {value(peek());}
    public void y() {value(peekY());}
    public void factorial() {value(factorial((int)Math.floor(stack.pop())));}
    private static double factorial(int n) {
        if (n < 2) return 1.;
        else return n * factorial(n - 1);
    }
}
