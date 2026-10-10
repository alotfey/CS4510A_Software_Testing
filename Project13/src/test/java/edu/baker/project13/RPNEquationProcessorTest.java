package edu.baker.project13;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Integration tests for the RPN calculator system.
 * Each test drives the real RPNEquationProcessor and the real RPNCalculator
 * together, using the script files in the test_cases folder as input.
 * Nothing is mocked, because the goal is to test how the two classes
 * work together, not each class on its own.
 */
class RPNEquationProcessorTest {

    /**
     * Runs every .rpn script in the test_cases folder through the processor
     * and checks that each script ends with a value within 1e-9 of 0.0.
     * Every script finishes by leaving abs(actual/expected - 1) on the stack,
     * which is the relative error of its calculations, so one assertion
     * works for every file. Files that do not end in .rpn are skipped,
     * because macOS can place hidden files such as .DS_Store in the folder.
     *
     * @throws IOException if the test_cases folder or a script file cannot be read
     */
    @Test
    void testProcessInput() throws IOException {
        Path path = FileSystems.getDefault().getPath("test_cases");
        // Files.list(path) gives every entry in the folder as a stream.
        // collect(toList()) turns that stream into a normal List we can loop over.
        for (Path p : Files.list(path).collect(toList())) {
            String fileName = p.getFileName().toString();
            if (fileName.endsWith(".rpn")) {
                System.out.println("Executing: " + p.toString());
                Reader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8);
                RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);
                double result = eqProcessor.processInput(new OutputStreamWriter(System.out));
                reader.close();
                assertEquals(0.0, result, 1e-9, "Script did not end near 0.0: " + fileName);
            }
        }
    }
    /**
     * Runs error_cases.rpn and checks that every error message the processor
     * can print appears in the script output. The output is captured in a
     * StringWriter instead of the console so the test can search it.
     * Each line of the script was chosen to reach a different error path in
     * processCommand(): the NoSuchElementException catch (Stack empty!), the
     * undefined-variable branch, the predefined-variable check, the
     * NumberFormatException catch (Illegal assignment), and the general
     * Exception catch, reached three different ways: ipow(0,0), x on an
     * empty stack, and an assignment with no value.
     *
     * @throws IOException if error_cases.rpn cannot be read
     */
    @Test
    void testProcessBadInput() throws IOException {
        Path path = FileSystems.getDefault().getPath("test_cases/error_cases.rpn");
        Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);
        StringWriter buffer = new StringWriter();
        eqProcessor.processInput(buffer);
        reader.close();

        String s = buffer.toString();
        System.out.println(s);
        assertTrue(s.contains("Stack empty!"), "Missing: Stack empty!");
        assertTrue(s.contains("Undefined variable: foo"), "Missing: Undefined variable");
        assertTrue(s.contains("Can't redefine pi"), "Missing: Can't redefine");
        assertTrue(s.contains("Illegal assignment: q = abc"), "Missing: Illegal assignment");
        assertTrue(s.contains("IllegalArgumentException: ipow(0,0) is indeterminate!"),
                "Missing: ipow(0,0) error");
        assertTrue(s.contains("NullPointerException"), "Missing: NullPointerException");
        assertTrue(s.contains("ArrayIndexOutOfBoundsException"),
                "Missing: ArrayIndexOutOfBoundsException");
    }
    /**
     * Runs a short script given as a String instead of a file, and sends the
     * output to a BufferedWriter, and checks that 2 3 add gives 5.0.
     * A StringReader is not a BufferedReader, so this reaches the branch in the
     * constructor that wraps the reader. A BufferedWriter reaches the branch in
     * processInput() that uses the writer as it is. The file-based tests always
     * take the other side of both checks.
     */
    @Test
    void testProcessInputRunsScriptFromString() {
        StringReader reader = new StringReader("2\n3\nadd\n");
        StringWriter output = new StringWriter();
        BufferedWriter writer = new BufferedWriter(output);
        RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);

        double result = eqProcessor.processInput(writer);

        assertEquals(5.0, result, 1e-9);
        assertTrue(output.toString().contains("5.0"), "Output should show 5.0");
    }

    /**
     * Checks that processInput() returns 0.0 when the script ends with an
     * empty stack. The script pushes 7 and then clears the stack, so the
     * return statement has nothing to peek at. None of the finished .rpn
     * scripts end with an empty stack, so this is the only test that takes
     * the "stack is empty" side of the return statement.
     */
    @Test
    void testProcessInputReturnsZeroWhenStackEndsEmpty() {
        StringReader reader = new StringReader("7\nclear\n");
        RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);

        double result = eqProcessor.processInput(new StringWriter());

        assertEquals(0.0, result, 0.0);
    }

    /**
     * Checks that processInput() stops cleanly, without crashing, when the
     * input cannot be read. Reading from a BufferedReader that has already
     * been closed throws an IOException, which is the only way to reach the
     * catch (IOException) block. The processor should stop, write nothing,
     * and return 0.0 because nothing reached the stack.
     *
     * @throws IOException if closing the reader fails
     */
    @Test
    void testProcessInputStopsWhenReaderFails() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader("5\n"));
        reader.close();
        RPNEquationProcessor eqProcessor = new RPNEquationProcessor(reader);
        StringWriter output = new StringWriter();

        double result = eqProcessor.processInput(output);

        assertEquals(0.0, result, 0.0);
        assertEquals("", output.toString());
    }

    /**
     * Checks that the three assert statements in RPNEquationProcessor reject
     * a null argument by throwing an AssertionError. These asserts are the
     * class's preconditions (design by contract, Module 2). Passing null on
     * purpose is the only way to reach their failure branch. This works
     * because Maven and IntelliJ both run tests with assertions turned on (-ea).
     */
    @Test
    void testNullArgumentsAreRejectedByAssertions() {
        // assertThrows runs the code after the arrow and passes only if that
        // code throws the named exception. The "() ->" part is a lambda: a
        // small block of code handed to assertThrows to run later.
        assertThrows(AssertionError.class, () -> new RPNEquationProcessor(null));

        RPNEquationProcessor eqProcessor = new RPNEquationProcessor(new StringReader(""));
        assertThrows(AssertionError.class, () -> eqProcessor.processInput(null));
        assertThrows(AssertionError.class, () -> eqProcessor.processCommand(null));
    }
}