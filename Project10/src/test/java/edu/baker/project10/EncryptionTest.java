package edu.baker.project10;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Encryption, the class that connects files to an EncryptionMethod.
 * Follows the prompt: encrypt the project's pom.xml into pom.xml.cipher,
 * decrypt that into pom.xml.clear, then compare the files byte by byte.
 *
 * @author Ahmed Lotfey
 */
public class EncryptionTest {

    /** The project's own pom.xml, used as the cleartext input file. */
    private static final Path CLEAR = Path.of("pom.xml");

    /** The encrypted file the test creates. */
    private static final Path CIPHER = Path.of("pom.xml.cipher");

    /** The decrypted file the test creates; it should match pom.xml exactly. */
    private static final Path CLEAR_AGAIN = Path.of("pom.xml.clear");

    /**
     * Supplies each EncryptionMethod to run through the file round trip,
     * with a readable name for the test report. Because Encryption depends only
     * on the interface, the same test works for every cipher.
     *
     * @return a Stream of Arguments: (display name, cipher to use)
     */
    static Stream<Arguments> encryptionMethods() {
        return Stream.of(
                Arguments.of("CaesarCipher", new CaesarCipher("Bubblegum")),
                Arguments.of("XORCipher", new XORCipher("Bubblegum"))
        );
    }

    /**
     * Verifies the full file round trip for one cipher: pom.xml.cipher is the
     * same length as pom.xml but most of its bytes are different, and
     * pom.xml.clear is identical to pom.xml.
     *
     * @param name   the cipher's name, used only in the test report
     * @param method the cipher passed to Encryption
     * @throws IOException if a file cannot be read or written
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("encryptionMethods")
    void encryptThenDecryptFileRestoresOriginal(String name, EncryptionMethod method) throws IOException {
        Encryption.encryptFile(CLEAR, CIPHER, method);
        Encryption.decryptFile(CIPHER, CLEAR_AGAIN, method);

        byte[] original  = Files.readAllBytes(CLEAR);
        byte[] encrypted = Files.readAllBytes(CIPHER);
        byte[] decrypted = Files.readAllBytes(CLEAR_AGAIN);

        // pom.xml vs pom.xml.cipher: same length, most bytes different
        assertEquals(original.length, encrypted.length);
        int differentBytes = 0;
        for (int i = 0; i < original.length; i++) {
            if (original[i] != encrypted[i]) {
                differentBytes++;
            }
        }
        assertTrue(differentBytes > original.length / 2,
                "only " + differentBytes + " of " + original.length + " bytes changed");

        // pom.xml vs pom.xml.clear: identical
        assertArrayEquals(original, decrypted);
    }

    /**
     * Verifies that encryptFile reports a missing input file by throwing
     * IOException instead of silently doing nothing, which is why Encryption
     * throws rather than catching and logging.
     */
    @Test
    void encryptFileThrowsIOExceptionWhenInputFileIsMissing() {
        Path missing = Path.of("no-such-file.txt");
        // assertThrows needs the code to run as a small block: () -> ... is a
        // lambda, Java's shorthand for "this piece of code". JUnit runs it and
        // checks that it throws an IOException.
        assertThrows(IOException.class, () -> Encryption.encryptFile(missing, CIPHER, new CaesarCipher("x")));
    }

    /**
     * Deletes the files the tests create so they do not pile up in the
     * project folder or end up in the submitted zip.
     *
     * @throws IOException if a file exists but cannot be deleted
     */
    @AfterEach
    void deleteGeneratedFiles() throws IOException {
        Files.deleteIfExists(CIPHER);
        Files.deleteIfExists(CLEAR_AGAIN);
    }

    /**
     * Verifies the file round trip with TranspositionCipher: pom.xml.clear is
     * identical to pom.xml. This cipher is not in encryptionMethods because it
     * only moves bytes: with 25341, positions 3 and 4 of every group never move,
     * and repeated characters such as indentation spaces often land on an equal
     * byte, so "most bytes different" is not guaranteed.
     *
     * @throws IOException if a file cannot be read or written
     */
    @Test
    void transpositionFileRoundTripRestoresOriginal() throws IOException {
        TranspositionCipher method = new TranspositionCipher("25341");
        Encryption.encryptFile(CLEAR, CIPHER, method);
        Encryption.decryptFile(CIPHER, CLEAR_AGAIN, method);
        assertArrayEquals(Files.readAllBytes(CLEAR), Files.readAllBytes(CLEAR_AGAIN));
    }

}