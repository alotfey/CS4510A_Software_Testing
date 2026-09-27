package edu.baker.project9;

/**
 * From https://stackoverflow.com/questions/4334808/how-could-i-read-java-console-output-into-a-string-buffer
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

/**
 * Captures everything printed to System.out between start() and stop() so a
 * test can assert on console output. While capturing, output still appears on
 * the real console as well. Supplied by the instructor for Assignment 9.
 */
public class ConsoleOutputCapturer {
    private ByteArrayOutputStream baos;
    private PrintStream previous;
    private boolean capturing;

    /**
     * Starts capturing System.out. Output is sent both to the original console
     * and to an in-memory buffer. Calling start() while already capturing does
     * nothing.
     */
    public void start() {
        if (capturing) {
            return;
        }

        capturing = true;
        previous = System.out;
        baos = new ByteArrayOutputStream();

        OutputStream outputStreamCombiner =
                new OutputStreamCombiner(Arrays.asList(previous, baos));
        PrintStream custom = new PrintStream(outputStreamCombiner);

        System.setOut(custom);
    }

    /**
     * Stops capturing, puts the original System.out back, and returns what was
     * printed since start().
     *
     * @return the captured console output, or an empty String if start() was
     *         not called first
     */
    public String stop() {
        if (!capturing) {
            return "";
        }

        System.setOut(previous);

        String capturedValue = baos.toString();

        baos = null;
        previous = null;
        capturing = false;

        return capturedValue;
    }

    /**
     * An OutputStream that copies every byte written to it into several other
     * OutputStreams (here: the real console and the capture buffer).
     */
    private static class OutputStreamCombiner extends OutputStream {
        private List<OutputStream> outputStreams;

        /**
         * Creates a combiner that forwards to the given streams.
         *
         * @param outputStreams the streams every byte is copied to
         */
        public OutputStreamCombiner(List<OutputStream> outputStreams) {
            this.outputStreams = outputStreams;
        }

        /**
         * Writes one byte to every stream in the list.
         *
         * @param b the byte to write
         * @throws IOException if any of the streams fails to write
         */
        public void write(int b) throws IOException {
            for (OutputStream os : outputStreams) {
                os.write(b);
            }
        }

        /**
         * Flushes every stream in the list.
         *
         * @throws IOException if any of the streams fails to flush
         */
        public void flush() throws IOException {
            for (OutputStream os : outputStreams) {
                os.flush();
            }
        }

        /**
         * Closes every stream in the list.
         *
         * @throws IOException if any of the streams fails to close
         */
        public void close() throws IOException {
            for (OutputStream os : outputStreams) {
                os.close();
            }
        }
    }
}