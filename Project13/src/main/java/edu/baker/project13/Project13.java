package edu.baker.project13;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * RPN Calculator Project
 * @author Richard Lesh
 */
public class Project13 {

    /** 
     * Pass in a relative file path argument to read from a file.
     * Otherwise reads from System.in.
     */
    public static void main(String[] args) {
        System.out.println("RPN Calculator!");
        Reader reader;
        try {
            if (args.length >= 1) {
                Path path = FileSystems.getDefault().getPath(args[0]);
                reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            } else {
                System.out.println("Enter one value or function name per line then <ENTER>.");
                System.out.println("Type 'Exit' then <ENTER> to quit.");
                reader = new InputStreamReader(System.in);
            }
            RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);
            double result = eqProcessor.processInput(new OutputStreamWriter(System.out));
        } catch (Exception ex) {
            System.out.println("An exception occured: " + 
                ex.getClass().getSimpleName() + " " + ex.getMessage());
        }
    }
}
