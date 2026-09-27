package edu.baker.project10;

/**
 * Implements a transposition cipher: the data is split into groups the same
 * size as the password, and the bytes inside each group are rearranged in the
 * order the password digits give. The bytes themselves are never changed, only
 * moved. A final group shorter than the password is left as it is, so the
 * output is always the same length as the input.
 * Example with password 25341: "Four " becomes "o urF" (positions 2,5,3,4,1).
 */
public class TranspositionCipher implements EncryptionMethod {
    /** order[i] is the 0-based position in the group that goes to output position i. */
    private int[] order;

    /**
     * Creates a transposition cipher with the given password.
     * @param password 5 to 9 digits, each used once, each from 1 to the password length
     * @throws IllegalArgumentException if the password is not valid
     */
    TranspositionCipher(String password) {
        setPassword(password);
    }

    /**
     * Checks and stores the password. It must be 5 to 9 digits long, every digit
     * must be between 1 and the password length, and no digit may repeat.
     * The stored key is only replaced if the whole password is valid.
     * @param password the digit string, for example "25341"
     * @throws IllegalArgumentException if the password is not valid
     */
    @Override
    public void setPassword(String password) {
        if (password.length() < 5 || password.length() > 9) {
            throw new IllegalArgumentException("Password must be 5 to 9 digits long");
        }
        int size = password.length();
        boolean[] used = new boolean[size + 1];
        int[] newOrder = new int[size];
        for (int i = 0; i < size; i++) {
            char c = password.charAt(i);
            if (c < '1' || c > '0' + size) {
                throw new IllegalArgumentException("Each digit must be between 1 and " + size);
            }
            int digit = c - '0';
            if (used[digit]) {
                throw new IllegalArgumentException("Digit " + digit + " is used more than once");
            }
            used[digit] = true;
            newOrder[i] = digit - 1;
        }
        order = newOrder;
    }

    /**
     * Rebuilds the password digits from the stored order.
     * @return the password as a digit string, for example "25341"
     */
    @Override
    public String getPassword() {
        String result = "";
        for (int i = 0; i < order.length; i++) {
            result = result + (order[i] + 1);
        }
        return result;
    }

    /**
     * Encrypts data by rearranging the bytes inside each full group: output
     * position i of a group takes the byte from group position order[i].
     * @param cleartext the bytes to encrypt
     * @return a new array of the same length holding the rearranged bytes
     */
    @Override
    public byte[] encrypt(byte[] cleartext) {
        byte[] ciphertext = new byte[cleartext.length];
        int size = order.length;
        int fullGroupsEnd = cleartext.length - (cleartext.length % size);
        for (int start = 0; start < fullGroupsEnd; start += size) {
            for (int i = 0; i < size; i++) {
                ciphertext[start + i] = cleartext[start + order[i]];
            }
        }
        for (int i = fullGroupsEnd; i < cleartext.length; i++) {
            ciphertext[i] = cleartext[i];   // leftover short group is not moved
        }
        return ciphertext;
    }

    /**
     * Decrypts data by putting every byte of each full group back in its
     * original position: the byte at group position i goes back to order[i].
     * @param ciphertext the bytes to decrypt
     * @return a new array of the same length holding the original bytes
     */
    @Override
    public byte[] decrypt(byte[] ciphertext) {
        byte[] cleartext = new byte[ciphertext.length];
        int size = order.length;
        int fullGroupsEnd = ciphertext.length - (ciphertext.length % size);
        for (int start = 0; start < fullGroupsEnd; start += size) {
            for (int i = 0; i < size; i++) {
                cleartext[start + order[i]] = ciphertext[start + i];
            }
        }
        for (int i = fullGroupsEnd; i < ciphertext.length; i++) {
            cleartext[i] = ciphertext[i];   // leftover short group is not moved
        }
        return cleartext;
    }
}