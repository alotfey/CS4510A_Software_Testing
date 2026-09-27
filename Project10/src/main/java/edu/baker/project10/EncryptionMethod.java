package edu.baker.project10;

/**
 * Abstraction for an encryption algorithm. High-level code such as Encryption
 * depends only on this interface, never on a specific cipher class, so any
 * cipher that implements it can be plugged in (Dependency Inversion).
 */
public interface EncryptionMethod {

    /**
     * Sets the password that controls how data is encrypted and decrypted.
     * An implementation may throw IllegalArgumentException if the password is
     * not valid for that cipher.
     * @param password the password to use
     */
    void setPassword(String password);

    /**
     * Returns a String representation of the internal password data, so a test
     * can confirm what setPassword actually stored.
     * @return the internal password data as a String
     */
    String getPassword();

    /**
     * Encrypts a block of data.
     * @param cleartext the bytes to encrypt
     * @return a new array holding the encrypted bytes
     */
    byte[] encrypt(byte[] cleartext);

    /**
     * Decrypts a block of data produced by encrypt with the same password.
     * @param ciphertext the bytes to decrypt
     * @return a new array holding the decrypted bytes
     */
    byte[] decrypt(byte[] ciphertext);
}