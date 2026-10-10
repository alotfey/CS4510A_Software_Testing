package edu.baker.project13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Class that processes a sequence of RPN calculator instructions.
 * @author Richard Lesh
 */
public class RPNEquationProcessor {
    private final RPNCalculator calc = new RPNCalculator();
    private final BufferedReader input;
    private final Map<String, Double> variables = new HashMap<>();
    
    public RPNEquationProcessor(Reader reader) {
        assert(reader != null);
        if (reader instanceof BufferedReader) {
            input = (BufferedReader)reader;
        } else {
            input = new BufferedReader(reader);
        }
    }
    
    public double processInput(Writer writer) {
        assert(writer != null);
        BufferedWriter buffer;
        if (writer instanceof BufferedWriter) {
            buffer = (BufferedWriter)writer;
        } else {
            buffer = new BufferedWriter(writer);
        }
        String line;
        while (true) {
            try {
                line = input.readLine();
                if (line == null) break;
                String result = processCommand(line.trim());
                if (result == null) break;
                buffer.write(result);
                buffer.newLine();
                buffer.flush();
            } catch (IOException ex) {
                break;
            }
        }
        return calc.isEmpty() ? 0.0 : calc.peek();
    }
    
    private static final Set<String> predefinedVariables = 
        new HashSet<>(List.of("x", "y", "e", "π", "pi"));
    public String processCommand(String cmd) {
        assert(cmd != null);
        if (cmd.startsWith("#")) return cmd;
        try {
            if (cmd.contains("=")) {
                String[] parts = cmd.split("=");
                String variable = parts[0].trim();
                if (predefinedVariables.contains(variable)) {
                    return "Can't redefine " + variable;
                }
                if (parts[1].trim().equalsIgnoreCase("x")) {
                    double x = calc.peek();
                    variables.put(variable, x);
                    return variable + "=" + Double.toString(x);
                }
                if (parts[1].trim().equalsIgnoreCase("y")) {
                    double y = calc.peekY();
                    variables.put(variable, y);
                    return variable + "=" + Double.toString(y);
                }
                variables.put(variable, Double.parseDouble(parts[1]));
                return cmd;
            }
            switch (cmd.toLowerCase()) {
                case "add": 
                case "+":       calc.add();break;
                case "sub": 
                case "-":       calc.sub();break;
                case "mult":
                case "*":       calc.mult();break;
                case "div":
                case "/":       calc.div();break;
                case "swap":    calc.swap();break;
                case "pop":     calc.rollDown();break;
                case "clear":   calc.clear();return "Clear";
                case "min":     calc.min();break;
                case "max":     calc.max();break;
                case "abs":     calc.abs();break;
                case "sign":    calc.sign();break;
                case "recip":   calc.recip();break;
                case "chs":
                case "neg":     calc.neg();break;
                case "floor":   calc.floor();break;
                case "ceil":    calc.ceil();break;
                case "round":   calc.round();break;
                case "trunc":   calc.trunc();break;
                case "!":
                case "fact":    calc.factorial();break;
                case "ipow":    calc.ipow();break;
                case "pow":     calc.pow();break;
                case "root":    calc.root();break;
                case "exp":     calc.exp();break;
                case "log":     calc.log();break;
                case "exp10":   calc.exp10();break;
                case "log10":   calc.log10();break;
                case "cos":     calc.cos();break;
                case "sin":     calc.sin();break;
                case "tan":     calc.tan();break;
                case "acos":    calc.acos();break;
                case "asin":    calc.asin();break;
                case "atan":    calc.atan();break;
                case "atan2":   calc.atan2();break;
                case "e":       calc.e();break;
                case "π":       calc.π();break;
                case "pi":      calc.π();break;
                case "x":       calc.x();break;
                case "y":       calc.y();break;
                case "":        return "";
                case "end":
                case "exit":
                case "quit":
                case "stop":    return null;
                default: 
                    try {
                        calc.value(Double.parseDouble(cmd));
                        break;
                    } catch (NumberFormatException ex) {
                        // Then this might be a variable
                    }
                    // Process variables
                    Double d = variables.get(cmd);
                    if (d != null) calc.value(d);
                    else return "Undefined variable: " + cmd;
            }
        } catch (NumberFormatException ex) {
            return "Illegal assignment: " + cmd;
        } catch (NoSuchElementException ex) {
            return "Stack empty!";
        } catch (Exception ex) {
            return ex.getClass().getSimpleName() + ": " + ex.getMessage();
        }
        return Double.toString(calc.peek());
    }
}
