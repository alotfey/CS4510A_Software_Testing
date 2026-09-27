/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.baker.project10;

/**
 * Implements a simple XOR cipher: each byte is XORed with one byte of the
 * password, cycling through the password bytes.
 * @author Richard Lesh
 */
public class XORCipher implements EncryptionMethod {
    private byte[] passwordBytes;

    /**
     * Creates an XOR cipher that uses the bytes of a password as the key.
     * @param password the password whose characters become the key bytes
     */
    XORCipher(String password) {
        setPassword(password);
    }

    /**
     * Stores the password as key bytes, one byte per character (the low
     * 8 bits of each character).
     * @param password the password whose characters become the key bytes
     */
    @Override
    public void setPassword(String password) {
        passwordBytes = new byte[password.length()];
        for (int i = 0; i < passwordBytes.length; ++i) {
            passwordBytes[i] = (byte)(password.charAt(i) & 0xFF);
        }
    }

    /**
     * Rebuilds a String from the stored key bytes, one character per byte.
     * For a plain ASCII password this is the same text that was set.
     * @return the stored key bytes as a String
     */
    @Override
    public String getPassword() {
        String result = "";
        for (int i = 0; i < passwordBytes.length; ++i) {
            result = result + (char)(passwordBytes[i] & 0xFF);
        }
        return result;
    }

    /**
     * Encrypts data by XORing each byte with the matching password byte,
     * repeating the password as many times as needed.
     * @param cleartext the bytes to encrypt
     * @return a new array of the same length holding the encrypted bytes
     */
    @Override
    public byte[] encrypt(byte[] cleartext) {
        byte[] ciphertext = new byte[cleartext.length];
        for (int i = 0; i < cleartext.length; ++i) {
            ciphertext[i] = (byte)(cleartext[i] ^ passwordBytes[i % passwordBytes.length]);
        }
        return ciphertext;
    }

    /**
     * Decrypts data by XORing each byte with the matching password byte.
     * XOR undoes itself, so this is the same operation as encrypt.
     * @param ciphertext the bytes to decrypt
     * @return a new array of the same length holding the decrypted bytes
     */
    @Override
    public byte[] decrypt(byte[] ciphertext) {
        byte[] cleartext = new byte[ciphertext.length];
        for (int i = 0; i < ciphertext.length; ++i) {
            cleartext[i] = (byte)(ciphertext[i] ^ passwordBytes[i % passwordBytes.length]);
        }
        return cleartext;
    }
}