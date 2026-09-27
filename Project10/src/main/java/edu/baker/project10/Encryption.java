package edu.baker.project10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Connects the file system to any EncryptionMethod. This is the only class that
 * reads or writes files; the ciphers only ever see byte arrays. Because it
 * depends on the EncryptionMethod interface, any cipher can be passed in.
 */
public class Encryption {

    /**
     * Reads a file, encrypts its bytes with the given method, and writes the
     * encrypted bytes to another file.
     * @param input  the file to encrypt
     * @param output the file to write the encrypted bytes to (created or replaced)
     * @param method the encryption method to use
     * @throws IOException if the input cannot be read or the output cannot be written
     */
    public static void encryptFile(Path input, Path output, EncryptionMethod method) throws IOException {
        byte[] cleartext = Files.readAllBytes(input);
        byte[] ciphertext = method.encrypt(cleartext);
        Files.write(output, ciphertext);
    }

    /**
     * Reads an encrypted file, decrypts its bytes with the given method, and
     * writes the decrypted bytes to another file.
     * @param input  the encrypted file to read
     * @param output the file to write the decrypted bytes to (created or replaced)
     * @param method the encryption method to use; it must have the same password
     *               that was used to encrypt
     * @throws IOException if the input cannot be read or the output cannot be written
     */
    public static void decryptFile(Path input, Path output, EncryptionMethod method) throws IOException {
        byte[] ciphertext = Files.readAllBytes(input);
        byte[] cleartext = method.decrypt(ciphertext);
        Files.write(output, cleartext);
    }
}