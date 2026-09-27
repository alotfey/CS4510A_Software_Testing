/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project10;

/**
 * Implements a simple shift cipher: every byte is shifted by the same amount,
 * which is derived from the password.
 * @author Richard Lesh
 */
public class CaesarCipher implements EncryptionMethod {
    private byte shiftBy;

    /**
     * Creates a Caesar cipher whose shift amount is computed from a password.
     * @param password the password used to compute the shift amount
     */
    CaesarCipher(String password) {
        setPassword(password);
    }

    /**
     * Computes and stores the shift amount from a password. A computed shift
     * of 0 would leave the data unchanged, so it is replaced by 3, the
     * original Caesar shift.
     * @param password the password used to compute the shift amount
     */
    @Override
    public void setPassword(String password) {
        shiftBy = (byte)(password.hashCode() & 0xFF - 128);
        if (shiftBy == 0) shiftBy = 3;  // The original Caesar shift
    }

    /**
     * Returns the stored shift amount as text. The Caesar cipher keeps only
     * the shift, not the password itself.
     * @return the shift amount, for example "19"
     */
    @Override
    public String getPassword() {
        return String.valueOf(shiftBy);
    }

    /**
     * Encrypts data by adding the shift amount to every byte.
     * @param cleartext the bytes to encrypt
     * @return a new array of the same length holding the encrypted bytes
     */
    @Override
    public byte[] encrypt(byte[] cleartext) {
        byte[] ciphertext = new byte[cleartext.length];
        for (int i = 0; i < cleartext.length; ++i) {
            ciphertext[i] = (byte)(cleartext[i] + shiftBy);
        }
        return ciphertext;
    }

    /**
     * Decrypts data by subtracting the shift amount from every byte.
     * @param ciphertext the bytes to decrypt
     * @return a new array of the same length holding the decrypted bytes
     */
    @Override
    public byte[] decrypt(byte[] ciphertext) {
        byte[] cleartext = new byte[ciphertext.length];
        for (int i = 0; i < ciphertext.length; ++i) {
            cleartext[i] = (byte)(ciphertext[i] - shiftBy);
        }
        return cleartext;
    }
}