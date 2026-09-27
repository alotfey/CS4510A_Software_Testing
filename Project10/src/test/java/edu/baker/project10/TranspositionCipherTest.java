package edu.baker.project10;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for TranspositionCipher, the extra encryption class from the
 * instructor's forum post. Uses the post's own example as the main known-answer
 * test, then checks password rules, the leftover-group rule, and two properties.
 *
 * @author Ahmed Lotfey
 */
public class TranspositionCipherTest {

    /**
     * Turns plain text into bytes so the tests can be written with readable strings.
     * @param text ASCII text
     * @return the text as a byte array, one byte per character
     */
    private static byte[] bytes(String text) {
        return text.getBytes(StandardCharsets.US_ASCII);
    }

    /**
     * Verifies the instructor's example exactly: with password 25341,
     * "Four Score and Seven" encrypts to "o urFceorSa nd enveS".
     */
    @Test
    void encryptMatchesInstructorExample() {
        TranspositionCipher cipher = new TranspositionCipher("25341");
        assertArrayEquals(bytes("o urFceorSa nd enveS"), cipher.encrypt(bytes("Four Score and Seven")));
    }

    /**
     * Verifies that decrypt turns the instructor's ciphertext back into
     * "Four Score and Seven".
     */
    @Test
    void decryptReversesInstructorExample() {
        TranspositionCipher cipher = new TranspositionCipher("25341");
        assertArrayEquals(bytes("Four Score and Seven"), cipher.decrypt(bytes("o urFceorSa nd enveS")));
    }

    /**
     * Verifies the leftover-group rule: "ABCDEFG" has one full group of 5
     * ("ABCDE" becomes "BECDA") and 2 leftover bytes ("FG") that stay in place.
     */
    @Test
    void encryptLeavesLeftoverShortGroupUnchanged() {
        TranspositionCipher cipher = new TranspositionCipher("25341");
        assertArrayEquals(bytes("BECDAFG"), cipher.encrypt(bytes("ABCDEFG")));
    }

    /**
     * Verifies the empty-input boundary: encrypting zero bytes returns an empty
     * array (no full groups and no leftover bytes).
     */
    @Test
    void encryptOfEmptyArrayReturnsEmptyArray() {
        TranspositionCipher cipher = new TranspositionCipher("25341");
        assertArrayEquals(new byte[0], cipher.encrypt(new byte[0]));
    }

    /**
     * Verifies that setPassword accepts valid passwords and getPassword reports
     * them back. Includes both length boundaries: 5 digits (shortest) and
     * 9 digits (longest).
     *
     * @param password a valid password
     */
    @ParameterizedTest
    @ValueSource(strings = {"25341", "12345", "54321", "123456789", "987654321"})
    void setPasswordAcceptsValidDigitStrings(String password) {
        TranspositionCipher cipher = new TranspositionCipher("12345");
        cipher.setPassword(password);
        assertEquals(password, cipher.getPassword());
    }

    /**
     * Verifies that setPassword rejects every kind of invalid password, one per
     * rule: empty, too short (4), too long (10), a digit beyond the length (6 in
     * a 5-digit key), a repeated digit, a non-digit, and a zero.
     *
     * @param password an invalid password
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "1234", "1234567891", "12346", "12234", "1234a", "01234"})
    void setPasswordRejectsInvalidPasswords(String password) {
        TranspositionCipher cipher = new TranspositionCipher("12345");
        assertThrows(IllegalArgumentException.class, () -> cipher.setPassword(password));
    }

    /**
     * Verifies that a rejected password does not damage the key already stored:
     * after a failed setPassword, getPassword still reports the old password.
     */
    @Test
    void failedSetPasswordKeepsPreviousPassword() {
        TranspositionCipher cipher = new TranspositionCipher("25341");
        assertThrows(IllegalArgumentException.class, () -> cipher.setPassword("12234"));
        assertEquals("25341", cipher.getPassword());
    }

    /**
     * Builds a random valid password: the digits 1 to size, shuffled.
     * @param size number of digits, 5 to 9
     * @param seed seed for the shuffle, so a failing case can be repeated
     * @return a password such as "31524"
     */
    private static String randomPassword(int size, long seed) {
        List<Integer> digits = new ArrayList<Integer>();
        for (int d = 1; d <= size; d++) {
            digits.add(d);
        }
        Collections.shuffle(digits, new Random(seed));
        String password = "";
        for (int i = 0; i < digits.size(); i++) {
            password = password + digits.get(i);
        }
        return password;
    }

    /**
     * Property (round trip): for any data and any valid password of 5 to 9
     * digits, decrypting the encrypted data gives back exactly the original bytes,
     * whether or not the length is a multiple of the password length.
     *
     * @param data random bytes generated by jqwik
     * @param size password length generated by jqwik, 5 to 9
     * @param seed shuffle seed generated by jqwik
     */
    @Property
    void decryptUndoesEncrypt(@ForAll byte[] data,
                              @ForAll @IntRange(min = 5, max = 9) int size,
                              @ForAll long seed) {
        TranspositionCipher cipher = new TranspositionCipher(randomPassword(size, seed));
        assertArrayEquals(data, cipher.decrypt(cipher.encrypt(data)));
    }

    /**
     * Property: a transposition cipher only moves bytes, it never changes them.
     * So after sorting, the ciphertext holds exactly the same bytes as the
     * cleartext. This is what makes it a transposition cipher and not a
     * substitution cipher like Caesar or XOR.
     *
     * @param data random bytes generated by jqwik
     * @param size password length generated by jqwik, 5 to 9
     * @param seed shuffle seed generated by jqwik
     */
    @Property
    void encryptOnlyRearrangesBytes(@ForAll byte[] data,
                                    @ForAll @IntRange(min = 5, max = 9) int size,
                                    @ForAll long seed) {
        TranspositionCipher cipher = new TranspositionCipher(randomPassword(size, seed));
        byte[] sortedClear = Arrays.copyOf(data, data.length);
        byte[] sortedCipher = cipher.encrypt(data);
        Arrays.sort(sortedClear);
        Arrays.sort(sortedCipher);
        assertArrayEquals(sortedClear, sortedCipher);
    }
}